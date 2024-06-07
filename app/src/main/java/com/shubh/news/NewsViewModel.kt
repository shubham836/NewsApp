package com.shubh.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubh.news.db.ArticleEntity
import com.shubh.news.model.Article
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {
    private val TAG = "NewsViewModel"
    var articlesList: MutableLiveData<NewsResult<List<Article>>> = MutableLiveData()
    var bookmarkAdded: MutableLiveData<Boolean> = MutableLiveData()
    var bookmarkDeleted: MutableLiveData<Boolean> = MutableLiveData()
    var allBookmarks: MutableLiveData<List<ArticleEntity>> = MutableLiveData()


    fun getTopHeadlines() {
        viewModelScope.launch {
            articlesList.postValue(NewsResult.Loading())
            val response = newsRepository.getTopHeadlines()
            if (response.isSuccessful && response.body() != null) {
                val articles =
                    response.body()!!.articles.map {
                        it.publishedAt = getTime(it.publishedAt)
                        it
                    }
                articlesList.postValue(NewsResult.Success(articles))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                articlesList.postValue(NewsResult.Error(message = errorObj.getString("message")))
            } else {
                articlesList.postValue(NewsResult.Error(message = "Something went wrong"))
            }
        }
    }

    fun getSearchNews(searchQuery: String) {
        viewModelScope.launch {
            articlesList.postValue(NewsResult.Loading())
            val response = newsRepository.getSearchedNews(searchQuery)
            if (response.isSuccessful && response.body() != null) {
                val articles =
                    response.body()!!.articles.map {
                        it.publishedAt = getTime(it.publishedAt)
                        it
                    }
                articlesList.postValue(NewsResult.Success(articles))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                articlesList.postValue(NewsResult.Error(message = errorObj.getString("message")))
            } else {
                articlesList.postValue(NewsResult.Error(message = "Something went wrong"))
            }
        }
    }

    fun addBookmark(article: ArticleEntity) {
        viewModelScope.launch {
            newsRepository.addBookmark(article)
            allBookmarks.postValue(newsRepository.getBookmarks())
        }
    }

    fun deleteBookmark(article: ArticleEntity) {
        viewModelScope.launch {
            newsRepository.deleteBookmark(article)
        }
    }

    fun getAllBookmark() {
        viewModelScope.launch {
            allBookmarks.postValue(newsRepository.getBookmarks())
        }
    }


    fun getTime(time: String): String {
        val instant = Instant.parse(time)
        val indiaZone = ZoneId.of("Asia/Kolkata")
        val zonedDateTime = instant.atZone(indiaZone)
        val formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))
        return formattedDateTime
    }
}
