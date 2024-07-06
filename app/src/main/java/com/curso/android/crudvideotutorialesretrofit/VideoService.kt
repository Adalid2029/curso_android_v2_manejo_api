package com.curso.android.crudvideotutorialesretrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface VideoService {
    @GET("v1/video-tutoriales")
    fun getAllVideos(): Call<List<Video>>

    @Multipart
    @POST("v1/video-tutoriales")
    fun addVideo(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("password") password: RequestBody,
        @Part("size") size: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<AddVideoResponse>


}
