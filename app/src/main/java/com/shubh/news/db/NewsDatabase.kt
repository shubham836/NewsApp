package com.shubh.news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1)
abstract class NewsDatabase() : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

}



