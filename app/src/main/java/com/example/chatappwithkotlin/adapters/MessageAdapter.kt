package com.example.chatappwithkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.model.Messages
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter (val contect : Context , val messageslist : ArrayList<Messages>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT = 1
    val ITEM_REC = 2

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
   if(viewType == 1){
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.sender_msg
               ,parent,false)

       return MessageAdapter.SentViewHolder(view)
   }else{
       val view : View = LayoutInflater.from(contect)
           .inflate(com.example.chatappwithkotlin.R.layout.reicever_msg
               ,parent,false)

       return MessageAdapter.ReceiveViewHolder(view)
   }

 }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java){
            val currentMessage = messageslist[position]
            val viewHolder = holder as SentViewHolder
            holder.txtmsgsent.text = currentMessage.msg


        }else{
            val currentMessage = messageslist[position]
            val viewHolder = holder as ReceiveViewHolder
            holder.txtmsgrec.text = currentMessage.msg

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageslist[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.uid)){
            return ITEM_SENT
        }else{
            return ITEM_REC
        }

    }
    override fun getItemCount(): Int {
        return messageslist.size
    }
}