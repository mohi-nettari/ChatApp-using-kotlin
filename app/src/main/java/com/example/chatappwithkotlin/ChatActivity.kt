package com.example.chatappwithkotlin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.adapters.MessageAdapter
import com.example.chatappwithkotlin.adapters.UserAdapter
import com.example.chatappwithkotlin.model.Imagemsg
import com.example.chatappwithkotlin.model.Messages
import com.example.chatappwithkotlin.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ChatActivity : AppCompatActivity() {

    lateinit var mDatabaseref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbstorage : FirebaseStorage
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
    private lateinit var backarrow : ImageView
    private lateinit var message : Message
    private lateinit var sendfiles : ImageView



    private lateinit var messagesrcv : RecyclerView
    private lateinit var adapter : MessageAdapter
    private lateinit var messageslist : ArrayList<Messages>
    private lateinit var images : String




    companion object{
        val IMAGE_SEND_CODE = 200
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
//
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDatabaseref = FirebaseDatabase.getInstance().getReference()
        mDbstorage = FirebaseStorage.getInstance()
        senderId = mAuth.currentUser!!.uid
        recieverId = intent.getStringExtra("recid").toString()
        userPic = intent.getStringExtra("image").toString()
        userName = intent.getStringExtra("name").toString()
        senderRoom = senderId+recieverId
        recieverRoom = recieverId+senderId
        backarrow = findViewById(R.id.backarrow)

        etmsg = findViewById(R.id.sendmsget)
        imageview = findViewById(R.id.profile_pic)
        txtname = findViewById(R.id.userename)
        sendarrow = findViewById(R.id.sendarrow)
        sendfiles = findViewById(R.id.sendfiles)
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
            sendMsg(msg,senderId,recieverId,false,"message")

        }

        backarrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        sendfiles.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_SEND_CODE)
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

    private fun sendMsg(msg: String, senderId: String, recieverId: String, b: Boolean , type : String) {
        etmsg.setText("")

        mDatabaseref.child("Chats")
            .child(senderRoom).child("messages").push()
            .setValue(Messages(msg, senderId, recieverId,false,"message"))
            .addOnSuccessListener {
                Toast.makeText(this@ChatActivity, "sended", Toast.LENGTH_SHORT).show()
                mDatabaseref.child("Chats").child(recieverRoom).child("messages").push()
                    .setValue(Messages(msg, senderId, recieverId,false,"message"))
                    .addOnSuccessListener {

                    }
            }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_SEND_CODE){
            if (data?.data != null && data != null) {

                val SImagePath = data.data
                images = SImagePath.toString()
//                val SImageBmp = MediaStore.Images.Media.getBitmap(contentResolver,SImagePath)
//                val OutputStream = ByteArrayOutputStream()
//
//                SImageBmp.compress(Bitmap.CompressFormat.JPEG,90,OutputStream)
//                val SImageBytes = OutputStream.toByteArray()


                val reference: StorageReference =
                    mDbstorage.getReference().child("ImageMessages")
                        .child(senderRoom)
                        .child(SImagePath.toString())


                reference.putFile(SImagePath!!).addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener { uri ->

                        mDatabaseref.child("Chats").child(senderRoom)
                            .child("messages").push()
                           .setValue(Messages(uri.toString(),senderId, recieverId,Calendar.getInstance().time,false,"image"))
                            .addOnSuccessListener {
                                Toast.makeText(this@ChatActivity, "sended image", Toast.LENGTH_SHORT).show()
                                mDatabaseref.child("Chats").child(recieverRoom)
                                    .child("messages").push()
                                    .setValue(Messages(uri.toString(),senderId, recieverId, Calendar.getInstance().time,false,"image"))

                                    .addOnSuccessListener {

                                    }
                            }


                        Toast.makeText(this@ChatActivity, "Image sended", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

}