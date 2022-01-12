package com.example.chatappwithkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.adapters.MessageAdapter
import com.example.chatappwithkotlin.adapters.UserAdapter
import com.example.chatappwithkotlin.model.Messages
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ChatActivity : AppCompatActivity() {

    lateinit var mDatabaseref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var senderId: String
    private lateinit var userPic: String
    private lateinit var userName: String
    private lateinit var recieverId: String
    private lateinit var isSeenListener: ValueEventListener
    private lateinit var senderRoom: String
    private lateinit var recieverRoom: String
    private  var state: Boolean =false

    private lateinit var etmsg : EditText
    private lateinit var txtname : TextView
    private lateinit var imageview : ImageView
    private lateinit var sendarrow : ImageView
    private lateinit var message : Message

    private lateinit var messagesrcv : RecyclerView
    private lateinit var adapter : MessageAdapter
    private lateinit var messageslist : ArrayList<Messages>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
//
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDatabaseref = FirebaseDatabase.getInstance().getReference()
        senderId = mAuth.currentUser!!.uid
        recieverId = intent.getStringExtra("recid").toString()
        userPic = intent.getStringExtra("image").toString()
        userName = intent.getStringExtra("name").toString()
        senderRoom = senderId+recieverId

        recieverRoom = recieverId+senderId

        etmsg = findViewById(R.id.sendmsget)
        imageview = findViewById(R.id.profile_pic)
        txtname = findViewById(R.id.userename)
        sendarrow = findViewById(R.id.sendarrow)

        messageslist = ArrayList()
        adapter = MessageAdapter(this,messageslist)
        messagesrcv =findViewById(R.id.rcvmessages)
        messagesrcv.layoutManager = LinearLayoutManager(this)
        messagesrcv.adapter = adapter


        txtname.setText(userName)
      //  updating the views in chat lyout
        Picasso.get().load(userPic).placeholder(R.drawable.avatar).into(imageview)

        displaymsgs()
        sendarrow.setOnClickListener{
            val  msg = etmsg.text.toString()
            if(msg.isEmpty()){
                return@setOnClickListener
            }
            sendMsg(msg,senderId,recieverId,false)

        }



    }


    private fun displaymsgs() {
        mDatabaseref.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageslist.clear()
                for (postSnapshot in snapshot.children){
                    val currentMessage = postSnapshot.getValue(Messages::class.java)
                    if (mAuth.currentUser?.uid == currentMessage?.uid
                        || mAuth.currentUser?.uid == currentMessage?.receiverid ){
                        messageslist.add(currentMessage!!)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun sendMsg(msg: String, senderId: String, recieverId: String, b: Boolean) {
        etmsg.setText("")

        mDatabaseref.child("Chats").child(senderRoom).child("messages").push()
            .setValue(Messages(msg, senderId, recieverId,false))
            .addOnSuccessListener {
                Toast.makeText(this@ChatActivity, "sended", Toast.LENGTH_SHORT).show()
                mDatabaseref.child("Chats").child(recieverRoom).child("messages").push()
                    .setValue(Messages(msg, senderId, recieverId,false))
                    .addOnSuccessListener {
                        Toast.makeText(this@ChatActivity, "hhhh", Toast.LENGTH_SHORT).show()

                    }
            }


    }




}