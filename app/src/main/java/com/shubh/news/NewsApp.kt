package com.shubh.news

import android.app.Application
import androidx.room.Room
import com.shubh.news.db.NewsDatabase

class NewsApp:Application() {
    val newsDB:NewsDatabase by lazy {
        Room.databaseBuilder(
            this,
            NewsDatabase::class.java,
            "NewsDB"
        ).build()
    }
}