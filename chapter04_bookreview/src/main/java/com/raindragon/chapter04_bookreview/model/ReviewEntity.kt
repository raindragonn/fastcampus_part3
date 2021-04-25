package com.raindragon.chapter04_bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Created by raindragonn on 2021/04/25.

@Entity
data class ReviewEntity(
    @PrimaryKey val id : Int?,
    @ColumnInfo(name = "review") val review: String?
)
