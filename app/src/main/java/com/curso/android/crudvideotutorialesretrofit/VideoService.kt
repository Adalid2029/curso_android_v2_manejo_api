package com.curso.android.crudvideotutorialesretrofit

import retrofit2.Call
import retrofit2.http.*

interface VideoService {
    @GET("api/v1/videos")
    fun getAllVideos(): Call<List<Video>>


}