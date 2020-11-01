package com.example.rickandmortykotlin.data.value_object


import com.google.gson.annotations.SerializedName

data class OriginX(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)