package com.example.chatappwithkotlin

import android.app.Activity
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



        //updating user information
        savebtn.setOnClickListener {
            val userName: String = usernameet.text.toString()
            val status: String = aboutet.text.toString()
            if (userName.isEmpty()) {
                return@setOnClickListener
            }

            val obj = HashMap<String, Any>()
            obj["name"] = userName
            obj["status"] = status
            mDb.getReference().child("Users").child(mAuth.currentUser!!.uid)
                .updateChildren(obj)
            Toast.makeText(this@SettingsActivity, "Your Information Saved", Toast.LENGTH_SHORT)
                .show()

        }

        //changing the password
        privacy.setOnClickListener {
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)

        }

        //getting user information
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

        //changing the user profile pic
        changepic.setOnClickListener {
//
           pickimage()
    }

//
    }
//
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



