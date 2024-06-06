package com.shubh.news.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{

    val loggingInterceptor= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val retrofit = Retrofit.Builder().baseUrl("https://gnews.io/api/v4/")
        .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

    val newsAPI by lazy { retrofit.create(NewsAPI::class.java) }
}