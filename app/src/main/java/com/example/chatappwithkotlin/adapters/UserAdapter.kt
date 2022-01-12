package com.example.chatappwithkotlin.adapters

import android.content.Context
import android.content.Intent
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithkotlin.adapters.UserAdapter.UserViewHolder
import com.example.chatappwithkotlin.model.User
//import com.google.android.gms.measurement.sdk.api.R
//import android.R
import android.widget.ImageView
import android.widget.Toast
import com.example.chatappwithkotlin.ChatActivity
import com.example.chatappwithkotlin.R
import com.squareup.picasso.Picasso


class UserAdapter(val context: Context , val userslist: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context)
            .inflate(com.example.chatappwithkotlin.R.layout.user_lay
                ,parent,false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentuser = userslist[position]

        holder.txtname.text = currentuser.name

        Picasso.get()
            .load(currentuser.image)
            .placeholder(R.drawable.avatar)
            .into(holder.profilepic)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("recid", currentuser.userid)
            intent.putExtra("image", currentuser.image)
            intent.putExtra("name", currentuser.name)
            context.startActivity(intent)
        }
        //holder.profilepic.text = currentuser.name



    }

    override fun getItemCount(): Int {
        return userslist.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtname = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.usernametxt)
        val txtlastmsg = itemView.findViewById<TextView>(com.example.chatappwithkotlin.R.id.lastmessagetxt)
        val profilepic = itemView.findViewById<ImageView>(com.example.chatappwithkotlin.R.id.profile_pic)
    }
}