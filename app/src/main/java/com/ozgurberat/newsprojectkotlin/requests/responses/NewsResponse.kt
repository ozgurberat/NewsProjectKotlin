package com.ozgurberat.newsprojectkotlin.requests.responses

import com.google.gson.annotations.SerializedName
import com.ozgurberat.newsprojectkotlin.model.Article

data class NewsResponse(
    @SerializedName("articles")
    val articleList: List<Article>? = null
)