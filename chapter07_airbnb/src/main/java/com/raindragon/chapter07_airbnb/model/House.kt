package com.raindragon.chapter07_airbnb.model

import com.google.gson.annotations.SerializedName

// Created by raindragonn on 2021/05/10.

data class House(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double

)
