package com.raindragon.chapter04_bookreview.model

import com.google.gson.annotations.SerializedName

// Created by raindragonn on 2021/04/24.

data class SearchBookDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("item")
    val books: List<BookModel>
)
