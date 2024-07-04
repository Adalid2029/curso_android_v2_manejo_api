package com.curso.android.crudvideotutorialesretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(private val videoList:List<Video>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>(){
    class VideoViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title :TextView = view.findViewById(R.id.videoTitle)
        val description :TextView = view.findViewById(R.id.videoDescription)
    }
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_list, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video.title
        holder.description.text = video.description
    }

    override fun getItemCount(): Int {
       return videoList.size
    }
}