package com.ozgurberat.newsprojectkotlin.requests.responses

import com.google.gson.annotations.SerializedName
import com.ozgurberat.newsprojectkotlin.model.Source

data class SourceResponse(
    @SerializedName("sources")
    val sourceList: List<Source>? = null
)