package com.shubh.news

import com.shubh.news.db.ArticleEntity
import com.shubh.news.db.NewsDatabase

class NewsRepository(val newsDB: NewsDatabase) {

    suspend fun getTopHeadlines() {}

    suspend fun getSearchedNews() {

    }

    suspend fun addBookmark(article: ArticleEntity) {
        newsDB.getArticleDao().addBookmark(article)
    }

    suspend fun getBookmarks() = newsDB.getArticleDao().getAllArticles()

    suspend fun deleteBookmark(article: ArticleEntity) {
        newsDB.getArticleDao().deleteBookmark(article)
    }
}