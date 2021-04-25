package com.raindragon.chapter04_bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Created by raindragonn on 2021/04/25.

@Entity
data class HistoryEntity(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "keyword") val keyword: String?
)
