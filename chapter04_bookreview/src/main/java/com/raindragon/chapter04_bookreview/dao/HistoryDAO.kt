package com.raindragon.chapter04_bookreview.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.raindragon.chapter04_bookreview.model.HistoryEntity

// Created by raindragonn on 2021/04/25.

@Dao
interface HistoryDAO {

    @Query("select * from historyentity")
    fun getAll(): List<HistoryEntity>

    @Insert
    fun insertHistory(historyEntity: HistoryEntity)

    @Query("delete from historyentity where keyword == :keyword")
    fun delete(keyword: String)
}