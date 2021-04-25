package com.raindragon.chapter04_bookreview

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.raindragon.chapter04_bookreview.dao.HistoryDAO
import com.raindragon.chapter04_bookreview.dao.ReviewDAO
import com.raindragon.chapter04_bookreview.model.HistoryEntity
import com.raindragon.chapter04_bookreview.model.ReviewEntity

// Created by raindragonn on 2021/04/25.

// room database version 관리 잘하기


@Database(entities = [HistoryEntity::class, ReviewEntity::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO
    abstract fun reviewDao(): ReviewDAO

    companion object {
        fun getInstance(context: Context): AppDataBase {
            val migration_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        "create table `REVIEW` (`id` INTEGER, `review` TEXT,PRIMARY KEY(`id`))"
                    )
                }
            }

            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "BookSearchDB"
            ).addMigrations(
                migration_1_2
            ).build()
        }
    }
}