package com.curso.android.crudvideotutorialesretrofit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity :AppCompatActivity() {
    private lateinit var btnGetVideos: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGetVideos = findViewById(R.id.btnGetVideos)
        btnGetVideos.setOnClickListener {
            startActivity(Intent(this, VideoListActivity::class.java))
        }
    }
}