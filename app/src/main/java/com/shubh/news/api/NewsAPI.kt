package com.shubh.news.api

import com.shubh.news.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("apikey") apiKey: String = "4c0c17d72c03cbcef6423ff84243ffdd"
    ): Call<NewsResponse>

    @GET("search")
    fun searchNews(
        @Query("q") query: String,
        @Query("apikey") apiKey: String = "4c0c17d72c03cbcef6423ff84243ffdd"
    ): Call<NewsResponse>
}