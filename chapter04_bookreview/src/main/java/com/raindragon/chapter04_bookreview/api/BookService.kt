package com.raindragon.chapter04_bookreview.api

import com.raindragon.chapter04_bookreview.model.BestSellerDto
import com.raindragon.chapter04_bookreview.model.BookModel
import com.raindragon.chapter04_bookreview.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Created by raindragonn on 2021/04/24.


interface BookService {
    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String,
    ): Call<SearchBookDTO>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String,
    ): Call<BestSellerDto>


}