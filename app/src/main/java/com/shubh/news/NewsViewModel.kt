package com.shubh.news

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shubh.news.api.RetrofitHelper
import com.shubh.news.model.Article
import com.shubh.news.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsViewModel() : ViewModel() {
    private val TAG = "NewsViewModel"
    var articlesList: MutableLiveData<List<Article>> = MutableLiveData()

    fun getTopHeadlines() {
        var call: Call<NewsResponse> = RetrofitHelper.newsAPI.getTopHeadlines()
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(p0: Call<NewsResponse>, response: Response<NewsResponse>) {
                try {
                    if (response.isSuccessful) {
                        val articles = response.body()!!.articles
                        articles.map {
                            it.publishedAt = getTime(it.publishedAt)
                        }
                        articlesList.value = articles
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onResponse: ${e.stackTraceToString()}")
                }
            }

            override fun onFailure(p0: Call<NewsResponse>, throwable: Throwable) {
                Log.e(TAG, "onFailure: ${throwable.message}")
            }

        })

    }

    fun searchNews(query: String) {
        var call: Call<NewsResponse> = RetrofitHelper.newsAPI.searchNews(query = query)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(p0: Call<NewsResponse>, response: Response<NewsResponse>) {
                try {
                    if (response.isSuccessful) {
                        val articles = response.body()!!.articles
                        articles.map {
                            it.publishedAt = getTime(it.publishedAt)
                        }
                        articlesList.value = articles
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onResponse: ${e.stackTraceToString()}")
                }
            }

            override fun onFailure(p0: Call<NewsResponse>, throwable: Throwable) {
                Log.e(TAG, "onFailure: ${throwable.message}")
            }
        })
    }

    fun getTime(time: String): String {
        val instant = Instant.parse(time)
        val indiaZone = ZoneId.of("Asia/Kolkata")
        val zonedDateTime = instant.atZone(indiaZone)
        val formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))
        return formattedDateTime
    }
}
