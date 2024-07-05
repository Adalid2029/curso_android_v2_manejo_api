package com.curso.android.crudvideotutorialesretrofit

import retrofit2.Call
import retrofit2.http.*

interface VideoService {
    @GET("v1/video-tutoriales")
    fun getAllVideos(): Call<List<Video>>


}
