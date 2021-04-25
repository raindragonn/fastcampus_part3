package com.raindragon.chapter04_bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raindragon.chapter04_bookreview.model.ReviewEntity

// Created by raindragonn on 2021/04/25.

@Dao
interface ReviewDAO {

    @Query("select * from ReviewEntity where id == :id")
    fun getReview(id: Int): ReviewEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: ReviewEntity)
}