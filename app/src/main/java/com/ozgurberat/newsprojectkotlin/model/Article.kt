package com.ozgurberat.newsprojectkotlin.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("source")
    val source: Source?= null,
    @SerializedName("title")
    val title: String?= null,
    @SerializedName("url")
    val url: String?= null,
    @SerializedName("urlToImage")
    val imageUrl: String?= null,
    @SerializedName("publishedAt")
    val publishedAt: String?= null,
)