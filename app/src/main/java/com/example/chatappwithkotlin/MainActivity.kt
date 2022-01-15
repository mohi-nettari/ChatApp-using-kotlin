package com.example.chatappwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.adapters.UserAdapter
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var userRcv : RecyclerView
    private lateinit var adapter : UserAdapter
    private lateinit var usrslist : ArrayList<User>
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth  = FirebaseAuth.getInstance()

        mDbref = FirebaseDatabase.getInstance().getReference()
        usrslist = ArrayList()
        adapter = UserAdapter(this,usrslist)
        userRcv = findViewById(R.id.rcvusers)
        userRcv.layoutManager = LinearLayoutManager(this)
        userRcv.adapter = adapter





        gettingTheusers()

    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
//
        if (firebaseUser == null) {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        if (!firebaseUser!!.isEmailVerified) {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            FirebaseAuth.getInstance().signOut()
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }
    }

    private fun gettingTheusers() {

        mDbref.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usrslist.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.userid ){
                        usrslist.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        });

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
       // return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                mAuth.signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            R.id.settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
//        if(item.itemId == R.id.logout){
//
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//            return true
//        }
//
//        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(true);
            mAuth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

//            val intent = Intent(Intent.ACTION_MAIN)
//            intent.addCategory(Intent.CATEGORY_HOME)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}