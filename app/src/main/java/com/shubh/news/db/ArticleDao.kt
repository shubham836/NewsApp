package com.shubh.news.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM ArticleEntity")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(article: ArticleEntity)

    @Delete
    suspend fun deleteBookmark(article: ArticleEntity)
}