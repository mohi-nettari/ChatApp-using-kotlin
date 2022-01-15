package com.example.chatappwithkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.model.Imagemsg
import com.example.chatappwithkotlin.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MessageAdapter (val contect : Context , val messageslist : ArrayList<Messages>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT = 1
    val ITEM_REC = 2
    val ITEM_SENT_IMG = 3
    val ITEM_RES_IMG = 4


    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtmsgsent = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.sender_txt)
        val txttimesent = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.sender_time)
        val txtseensent = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.sender_seen)
    }
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtmsgrec = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.receiver_txt)
        val txttimerec = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.receiver_time)
        val txtseenrec = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.receiver_seen)
    }

    class SentImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  simg = itemView.findViewById<ImageView>(com.example.chatappwithkotlin.R.id.simg)
        val stxttime  = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.stxttimee)
    }
    class RecImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  rimg= itemView.findViewById<ImageView>(com.example.chatappwithkotlin.R.id.rimg)
        val  rtxttime= itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.rtxttimee)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
   if(viewType == 1){
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.sender_msg
               ,parent,false)

       return MessageAdapter.SentViewHolder(view)
   }else if (viewType == 2){
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.reicever_msg
               ,parent,false)

       return MessageAdapter.ReceiveViewHolder(view)
   }
   else if (viewType == 4){
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.receiver_image_message
               ,parent,false)

       return MessageAdapter.RecImageViewHolder(view)
   }
   else {
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.sender_image_message
               ,parent,false)

       return MessageAdapter.SentImageViewHolder(view)
   }

 }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java){
            val currentMessage = messageslist[position]
            val viewHolder = holder as SentViewHolder
            holder.txtmsgsent.text = currentMessage.msg


        }else if(holder.javaClass == ReceiveViewHolder::class.java){
            val currentMessage = messageslist[position]
            val viewHolder = holder as ReceiveViewHolder
            holder.txtmsgrec.text = currentMessage.msg

        }
        else if (holder.javaClass == SentImageViewHolder::class.java){
            val curremtImage = messageslist[position]
            val viewHolder = holder as SentImageViewHolder
            holder.stxttime.text = curremtImage.timstamp.toString()
            Picasso.get()
                .load(curremtImage.msg)
                .into(holder.simg)

        }
        else if (holder.javaClass == RecImageViewHolder::class.java){
            val curremtImage = messageslist[position]
            val viewHolder = holder as RecImageViewHolder
            holder.rtxttime.text = curremtImage.timstamp.toString()

            Picasso.get()
                .load(curremtImage.msg)
              .placeholder(com.example.chatappwithkotlin.R.drawable.ic_image_24)
                .into(holder.rimg)

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageslist[position]
        if(FirebaseAuth.getInstance().currentUser?.uid
                .equals(currentMessage.uid)&& currentMessage.type != "image"){
            return ITEM_SENT
            }
            else if(!(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.uid)) && currentMessage.type != "image"){
            return ITEM_REC
            }
            else if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.uid) && currentMessage.type == "image"){
                return ITEM_SENT_IMG
                }
                else{
            return ITEM_RES_IMG

        }

        }



    override fun getItemCount(): Int {
        return messageslist.size
    }
}

