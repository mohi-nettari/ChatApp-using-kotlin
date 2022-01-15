package com.example.chatappwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.HashMap

class PrivacyActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbref : DatabaseReference
    private lateinit var mDb : FirebaseDatabase

    private lateinit var passet : EditText
    private lateinit var passet2 : EditText
    private lateinit var change : Button
    private lateinit var dntchange : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        mAuth = FirebaseAuth.getInstance()
        mDb = FirebaseDatabase.getInstance()
        mDbref = FirebaseDatabase.getInstance().getReference()

        passet = findViewById(R.id.passet)
        passet2 = findViewById(R.id.passet2)
        change = findViewById(R.id.changepass)
        dntchange = findViewById(R.id.dentchangepass)

        dntchange.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        //getting user information
        mDb.getReference().child("Users")
            .child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user1: User? = snapshot.getValue(User::class.java)
                    passet.setText(user1?.pass)
                    passet2.setText(user1?.pass)


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        change.setOnClickListener {
            val pass1: String = passet.text.toString()
            val pass2 : String = passet2.text.toString()
            if (pass1.isEmpty()|| pass2.isEmpty()) {
                Toast.makeText(this@PrivacyActivity, "Enter your pass twice", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!pass1.equals(pass2)) {
                Toast.makeText(this@PrivacyActivity
                    , "Passwords that you entered are not the same "
                    , Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            val obj = HashMap<String, Any>()
            obj["pass"] = pass1
            mDb.getReference().child("Users").child(mAuth.currentUser!!.uid)
                .updateChildren(obj).addOnSuccessListener {

                    Toast.makeText(this@PrivacyActivity, "Your password changed", Toast.LENGTH_SHORT)
                        .show()
//                    val intent = Intent(this, SettingsActivity::class.java)
//                    startActivity(intent)
               }



        }



    }
}