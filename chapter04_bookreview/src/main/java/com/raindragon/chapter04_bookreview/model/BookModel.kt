package com.raindragon.chapter04_bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Created by raindragonn on 2021/04/24.

@Parcelize
data class BookModel(
    @SerializedName("itemId")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("coverSmallUrl")
    val coverSmallUrl: String
) : Parcelable
