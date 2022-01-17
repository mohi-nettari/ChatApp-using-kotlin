package com.example.chatappwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ViewImageActivity : AppCompatActivity() {
    private lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        supportActionBar?.hide()

        img = findViewById(R.id.imgshow)
        val imageuri = intent.getStringExtra("img").toString()

        Picasso.get()
            .load(imageuri)
            .into(img)

    }
}