package com.raindragon.chapter04_bookreview

import com.raindragon.chapter04_bookreview.api.BookService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Created by raindragonn on 2021/04/24.

object Api {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://book.interpark.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    lateinit var bookService_: BookService

    val bookService: BookService
        get() {
            if (::bookService_.isInitialized.not())
                bookService_ = retrofit.create(BookService::class.java)
            return bookService_
        }
}