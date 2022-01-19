package com.example.chatappwithkotlin

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.adapters.MessageAdapter
import com.example.chatappwithkotlin.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    lateinit var mDatabaseref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbstorage: FirebaseStorage
    private lateinit var senderId: String
    private lateinit var userPic: String
    private lateinit var userName: String
    private lateinit var recieverId: String
    private lateinit var isSeenListener: ValueEventListener
    private lateinit var senderRoom: String
    private lateinit var recieverRoom: String
    private var state: Boolean = false

    private lateinit var etmsg: EditText
    private lateinit var txtname: TextView
    private lateinit var imageview: ImageView
    private lateinit var sendarrow: ImageView
    private lateinit var backarrow: ImageView
    private lateinit var message: Message
    private lateinit var sendfiles: ImageView


    private lateinit var messagesrcv: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var messageslist: ArrayList<Messages>
    private lateinit var images: String


    companion object {
        val IMAGE_SEND_CODE = 200
        val PICK_IMAGES_CODE = 500
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
//

        //initialization
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseref = FirebaseDatabase.getInstance().getReference()
        mDbstorage = FirebaseStorage.getInstance()
        senderId = mAuth.currentUser!!.uid
        recieverId = intent.getStringExtra("recid").toString()
        userPic = intent.getStringExtra("image").toString()
        userName = intent.getStringExtra("name").toString()
        senderRoom = senderId + recieverId
        recieverRoom = recieverId + senderId
        backarrow = findViewById(R.id.backarrow)

        etmsg = findViewById(R.id.sendmsget)
        imageview = findViewById(R.id.profile_pic)
        txtname = findViewById(R.id.userename)
        sendarrow = findViewById(R.id.sendarrow)
        sendfiles = findViewById(R.id.sendfiles)
        messageslist = ArrayList()
        adapter = MessageAdapter(this, messageslist,recieverId)
        messagesrcv = findViewById(R.id.rcvmessages)
        messagesrcv.layoutManager = LinearLayoutManager(this)
        messagesrcv.adapter = adapter

        displaymsgs()

        onEnterKey()

        txtname.setText(userName)
        //  updating the views in chat lyout
        Picasso.get().load(userPic).placeholder(R.drawable.avatar).into(imageview)

        //send arrow event
        sendarrow.setOnClickListener {
            val msg = etmsg.text.toString()
            if (msg.isEmpty()) {
                return@setOnClickListener
            }
            sendMsg(msg, senderId, recieverId, false, "message")
            messagesrcv.scrollToPosition(messageslist.size - 1);
        }

        //back arrow event
        backarrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        //send pics event
        sendfiles.setOnClickListener {
//            AlertDialog.Builder(this)
//                .setTitle("Pick")
//                .setMessage("Are you sure want to send one or multiple images")
//                .setPositiveButton("Multiple") { dialogInterface, i ->
//                    pickmultipleimages()
//                }.setNegativeButton(
//                    "One"
//                ) { dialogInterface, i ->
//                    pickimage()
//                }.show()
            pickmultipleimages()

        }




    }





    //handling on enter key click event
    private fun onEnterKey() {

        etmsg.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val msg = etmsg.text.toString()
                if (msg.isEmpty()) {
                    return@OnKeyListener false
                }
                sendMsg(msg, senderId, recieverId, false, "message")
                messagesrcv.scrollToPosition(messageslist.size - 1);

                return@OnKeyListener true
            }

            false
        })

    }

    //picking an  image from the galary
    private fun pickimage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_SEND_CODE)

    }

    //picking an  image from the galary
    private fun pickmultipleimages() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "choose image(s)"), PICK_IMAGES_CODE)

    }


    //displaying messages from the database on the recycler view
    private fun displaymsgs() {
        mDatabaseref.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageslist.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentMessage = postSnapshot.getValue(Messages::class.java)
                        if (mAuth.currentUser?.uid == currentMessage?.uid
                            || mAuth.currentUser?.uid == currentMessage?.receiverid
                        ) {
                            currentMessage!!.messageid = postSnapshot.getValue().toString()
                            messageslist.add(currentMessage!!)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    messagesrcv.scrollToPosition(messageslist.size - 1);

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })


    }


    //sending messages to the database
    private fun sendMsg(msg: String, senderId: String, recieverId: String, b: Boolean, type: String) { etmsg.setText("")
        val calendar = Calendar.getInstance()
        val currentdate = SimpleDateFormat("MM/DD /yyyy")
        val  savecurrentdate = currentdate.format(calendar.time).toString()
        val currenttime = SimpleDateFormat("HH:MM a")
       val savecurrenttime = currenttime.format(calendar.time).toString()

        mDatabaseref.child("Chats")
            .child(senderRoom).child("messages").push()
            .setValue(Messages(msg, senderId, recieverId, savecurrentdate+"\n"+savecurrenttime , false, "message"))
            .addOnSuccessListener {
                mDatabaseref.child("Chats").child(recieverRoom).child("messages").push()
                    .setValue(Messages(msg, senderId, recieverId, savecurrentdate+"\n"+savecurrenttime ,false, "message"))
                    .addOnSuccessListener {
                        messagesrcv.scrollToPosition(messageslist.size - 1);

                    }
            }


    }


    //after the user chooses a pic to send
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_SEND_CODE) {
            if (data?.data != null && data != null) {

                val SImagePath = data.data
                images = SImagePath.toString()
//                val SImageBmp = MediaStore.Images.Media.getBitmap(contentResolver,SImagePath)
//                val OutputStream = ByteArrayOutputStream()
//
//                SImageBmp.compress(Bitmap.CompressFormat.JPEG,90,OutputStream)
//                val SImageBytes = OutputStream.toByteArray()

                val calendar = Calendar.getInstance()
                val currentdate = SimpleDateFormat("MM/DD /yyyy")
                val  savecurrentdate = currentdate.format(calendar.time).toString()
                val currenttime = SimpleDateFormat("HH:MM a")
                val savecurrenttime = currenttime.format(calendar.time).toString()

                val reference: StorageReference =
                    mDbstorage.getReference().child("ImageMessages")
                        .child(senderRoom)
                        .child(savecurrentdate+savecurrenttime)


                reference.putFile(SImagePath!!).addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener { uri ->

                        mDatabaseref.child("Chats").child(senderRoom)
                            .child("messages").push()
                            .setValue(
                                Messages(
                                    uri.toString(),
                                    senderId,
                                    recieverId,
                                    savecurrentdate+"\n"+
                                            savecurrenttime,
                                    false,
                                    "image"
                                )
                            )
                            .addOnSuccessListener {
                                mDatabaseref.child("Chats").child(recieverRoom)
                                    .child("messages").push()
                                    .setValue(
                                        Messages(
                                            uri.toString(),
                                            senderId,
                                            recieverId,
                                            savecurrentdate+"\n"+
                                                    savecurrenttime,
                                            false,
                                            "image"
                                        )
                                    )

                                    .addOnSuccessListener {

                                    }
                            }
                    }
                }

                messagesrcv.scrollToPosition(messageslist.size - 1);

            }
            messagesrcv.scrollToPosition(messageslist.size - 1);

        }


        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGES_CODE) {

            if (data!!.clipData != null && data != null) {
            val count = data.clipData!!.itemCount
                for (i in 0 until count){
                    val SImagePath = data.clipData!!.getItemAt(i).uri
                    images = SImagePath.toString()
//                val SImageBmp = MediaStore.Images.Media.getBitmap(contentResolver,SImagePath)
//                val OutputStream = ByteArrayOutputStream()
//
//                SImageBmp.compress(Bitmap.CompressFormat.JPEG,90,OutputStream)
//                val SImageBytes = OutputStream.toByteArray()
                    val reference: StorageReference =
                        mDbstorage.getReference().child("ImageMessages")
                            .child(senderRoom)
                            .child(images)

                    reference.putFile(SImagePath!!).addOnSuccessListener {
                        reference.downloadUrl.addOnSuccessListener { uri ->

                            mDatabaseref.child("Chats").child(senderRoom)
                                .child("messages").push()
                                .setValue(
                                    Messages(
                                        uri.toString(),
                                        senderId,
                                        recieverId,
                                        Calendar.getInstance().time.toString(),
                                        false,
                                        "image"
                                    )
                                )
                                .addOnSuccessListener {
                                    mDatabaseref.child("Chats").child(recieverRoom)
                                        .child("messages").push()
                                        .setValue(
                                            Messages(
                                                uri.toString(),
                                                senderId,
                                                recieverId,
                                                Calendar.getInstance().time.toString(),
                                                false,
                                                "image"
                                            )
                                        )
                                        .addOnSuccessListener {

                                        }
                                }
                        }
                    }
                    messagesrcv.scrollToPosition(messageslist.size - 1);

                }
            }else{
Toast.makeText(this@ChatActivity,"nulll",Toast.LENGTH_SHORT).show()
                messagesrcv.scrollToPosition(messageslist.size - 1);

            }
            messagesrcv.scrollToPosition(messageslist.size - 1);

        }


    }
}



