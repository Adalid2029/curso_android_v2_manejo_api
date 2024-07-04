package com.curso.android.crudvideotutorialesretrofit

data class Video (
    val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val createdAt: String
)