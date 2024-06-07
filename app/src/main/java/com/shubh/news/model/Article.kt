package com.shubh.news.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Article(
    val description: String,
    val image: String,
    var publishedAt: String,
    val title: String,
    val url: String
) : Parcelable

