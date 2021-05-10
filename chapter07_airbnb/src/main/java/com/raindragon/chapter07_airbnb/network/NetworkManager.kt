package com.raindragon.chapter07_airbnb.network

import com.raindragon.chapter07_airbnb.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// Created by raindragonn on 2021/05/10.

object NetworkManager {
    private val houseRetrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Config.HOUSE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val houseClient by lazy { houseRetrofit.create<HouseApi>() }
}