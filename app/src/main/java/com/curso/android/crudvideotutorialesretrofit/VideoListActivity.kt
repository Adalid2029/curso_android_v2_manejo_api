package com.curso.android.crudvideotutorialesretrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoListActivity : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var videoService: VideoService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        recyclerview = findViewById(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)

        videoService =
            RetrofitClient.getClient("https://api.stacknews.xyz/").create(VideoService::class.java)

        videoService.getAllVideos().enqueue(object : Callback<List<Video>> {
            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                if (response.isSuccessful) {
                    val videos = response.body() ?: emptyList()
                    videoAdapter = VideoAdapter(videos)
                    recyclerview.adapter = videoAdapter
                }
            }

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
               Log.d("VideoListActivity","Error al traer los videotutoriales")
            }

        })
    }
}