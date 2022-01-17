package com.example.chatappwithkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatappwithkotlin.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import okhttp3.internal.Version
import java.util.HashMap
import java.util.jar.Manifest
import android.os.Build.VERSION.SDK_INT
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbref : DatabaseReference
    private lateinit var mDb : FirebaseDatabase
    private lateinit var mDbstorage : FirebaseStorage
    private lateinit var usernameet : EditText
    private lateinit var aboutet : EditText
    private lateinit var savebtn : Button
    private lateinit var profilepic : ImageView
    private lateinit var changepic : ImageView
    private lateinit var privacy : TextView
    private lateinit var backarrow : ImageView


    companion object{
    val IMAGE_SEQUST_CODE = 100
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //initializing the widgets
        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseDatabase.getInstance()
        mDbref = FirebaseDatabase.getInstance().getReference()
        mDbstorage = FirebaseStorage.getInstance()

        usernameet = findViewById(R.id.usernameet)
        aboutet = findViewById(R.id.aboutet)
        savebtn = findViewById(R.id.savebtn)
        profilepic = findViewById(R.id.profilepic)
        changepic = findViewById(R.id.changepic)
        privacy = findViewById(R.id.Privacy)

        backarrow = findViewById(R.id.backarrowset)


        supportActionBar?.hide()

        //getting users info
        gettingusersinfo()

        //on enter key event
        onEnterKey()

        //back arrow event
        backarrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //updating user information
        savebtn.setOnClickListener {
           updateuserinfo()
        }


        //changing the password
        privacy.setOnClickListener {
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)

        }




        //changing the user profile pic
        changepic.setOnClickListener {
//
           pickimage()
    }




    }


    //updating user's information
    private fun updateuserinfo(){
        val userName: String = usernameet.text.toString()
        val status: String = aboutet.text.toString()
        if (userName.isEmpty()) {
            return
        }

        val obj = HashMap<String, Any>()
        obj["name"] = userName
        obj["status"] = status
        mDb.getReference().child("Users").child(mAuth.currentUser!!.uid)
            .updateChildren(obj)
        Toast.makeText(this@SettingsActivity, "Your Information Saved", Toast.LENGTH_SHORT)
            .show()
        hideMyKeyBoard()
    }



    //getting user information
    private fun gettingusersinfo(){
        mDb.getReference().child("Users")
            .child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user1: User? = snapshot.getValue(User::class.java)
                    Picasso.get()
                        .load(user1?.image)
                        .placeholder(R.drawable.avatar)
                        .into(profilepic)

                    usernameet.setText(user1?.name)
                    aboutet.setText(user1?.status)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    //handling on enter key click event
private fun onEnterKey(){
    aboutet.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
            updateuserinfo()
            return@OnKeyListener true
        }

        false
    })

}

    //hiding yhe keyboard
    private fun hideMyKeyBoard(){
        val view = this.currentFocus

            if(view != null){
                  val hidemode = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hidemode.hideSoftInputFromWindow(view.windowToken,0)
                }

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun pickimage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_SEQUST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_SEQUST_CODE){
            if (data?.data != null) {
                val sFile = data.data

                profilepic.setImageURI(sFile)

                val reference: StorageReference =
                    mDbstorage.getReference().child("Profile_pics")
                        .child(mAuth.currentUser!!.uid)

                reference.putFile(sFile!!).addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener { uri ->

                        mDb.getReference().child("Users").child(mAuth.currentUser!!.uid)
                            .child("image").setValue(uri.toString())

                        Toast.makeText(this@SettingsActivity, "Profile pic Updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        }
    }


}



