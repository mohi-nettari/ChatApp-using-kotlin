package com.example.chatappwithkotlin

import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase




class LoginActivity : AppCompatActivity() {

       private lateinit var etemail : EditText
    private lateinit var etpass : EditText
    private lateinit var btnsiginin : Button
    private lateinit var btnsignup : Button

    private lateinit var mAuth : FirebaseAuth
    private lateinit var  mDBref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        mDBref = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()

        //initializing the widgets
        etemail = findViewById(R.id.login_email_et)
        etpass = findViewById(R.id.login_pass_et)
        btnsiginin = findViewById(R.id.btnsigninlogin)
        btnsignup = findViewById(R.id.btnsignuplogin)


        btnsignup.setOnClickListener {
            val intent = Intent(this, RegestrationActivity::class.java)
            startActivity(intent)

        }

        btnsiginin.setOnClickListener {
            val pass = etpass.text.toString()
            val email = etemail.text.toString()
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this@LoginActivity,"Your Email is wrong", Toast.LENGTH_SHORT).show()
            }
            if(email.isEmpty()){
                Toast.makeText(this@LoginActivity,"Pleas enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pass.isEmpty()||pass.length < 6){
                Toast.makeText(this@LoginActivity,"pleas enter a password that its size more than 6", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            login(email,pass)

        }


    }

    override fun onStart() {
        super.onStart()
        var firebaseUser = FirebaseAuth.getInstance().currentUser

        //   String id = firebaseUser.getUid();
//        checking for users existing : saving users
        if (firebaseUser != null) {
            mDBref.child("Users").child(firebaseUser.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                });

        }
    }
        private fun login(email: String, pass: String) {
            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result.user
                        if(firebaseUser!!.isEmailVerified){
                            mDBref.child("Users").child(mAuth.uid.toString()).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                   val currentUser = snapshot.getValue(User::class.java)
                                    if(currentUser!!.pass == etpass.text.toString()){
                                        val  intent = Intent(this@LoginActivity,MainActivity::class.java)
                                        startActivity(intent)
                                    }else{
                                        Toast.makeText(this@LoginActivity,"the password you entered is wrong",Toast.LENGTH_SHORT).show()

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }


                            });



                        }else{
                        mAuth.signOut()
                        val  intent = Intent(this@LoginActivity,LoginActivity::class.java)
                        startActivity(intent)
                            finish()

                            Toast.makeText(this@LoginActivity,"Your email is not verified\n go check your email to verify it",Toast.LENGTH_SHORT).show()
                        }



                    } else {
                        Toast.makeText(this@LoginActivity,"Error", Toast.LENGTH_SHORT).show()

                    }
                }


        }

}