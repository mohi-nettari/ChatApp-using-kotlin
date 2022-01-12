package com.example.chatappwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegestrationActivity : AppCompatActivity() {

    private lateinit var etemail : EditText
    private lateinit var etpass : EditText
    private lateinit var etpass2 : EditText
    private lateinit var etname : EditText

    private lateinit var btnsiginin : Button
    private lateinit var btnsignup : Button

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regestration)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        //initializing the widgets
        etemail = findViewById(R.id.regester_email_et)
        etpass = findViewById(R.id.regester_pass_et)
        etpass2 = findViewById(R.id.regester_pass_et2)
        etname = findViewById(R.id.regester_name_et)

        btnsiginin = findViewById(R.id.btnsigninreg)
        btnsignup = findViewById(R.id.btnsignupreg)


        btnsiginin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        btnsignup.setOnClickListener {
            val pass = etpass.text.toString()
            val email = etemail.text.toString()
            val name = etname.text.toString()
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this@RegestrationActivity,"Your Email is wrong", Toast.LENGTH_SHORT).show()
            }
            if(email.isEmpty()){
                Toast.makeText(this@RegestrationActivity,"Pleas enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(name.isEmpty()){
                Toast.makeText(this@RegestrationActivity,"pleas enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener


            }
            if(pass.isEmpty()||pass.length < 6){
                Toast.makeText(this@RegestrationActivity,"pleas enter a password that its size more than 6", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if(pass!=etpass2.text.toString()){
                Toast.makeText(this@RegestrationActivity,"passwords that you enter are not the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            creatacc(email,pass,name)
        }
    }

    private fun creatacc(email: String, pass: String , name : String) {
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = mAuth.uid
                    mAuth.currentUser!!.sendEmailVerification()
                    addUserToDB(email,name,pass,mAuth.currentUser?.uid!!)
                    val intent = Intent(this@RegestrationActivity, LoginActivity::class.java)
                    Toast.makeText(this@RegestrationActivity,"we sent a verification email to your email", Toast.LENGTH_SHORT).show()

                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@RegestrationActivity,"Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDB(email: String, name: String, pass: String, uid: String) {
        mDbref = FirebaseDatabase.getInstance().getReference()
        mDbref.child("Users").child(uid).setValue(User(name,email,pass,uid))


    }
}

