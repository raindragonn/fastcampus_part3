package com.raindragon.chapter07_airbnb.network

import com.raindragon.chapter07_airbnb.model.HouseDto
import retrofit2.Call
import retrofit2.http.GET

// Created by raindragonn on 2021/05/10.

interface HouseApi {
    @GET("/v3/e41162ff-5bef-4447-9246-a16270aa92d6")
    fun getHouseList(): Call<HouseDto>
}