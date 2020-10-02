package com.ozgurberat.newsprojectkotlin.requests

import com.ozgurberat.newsprojectkotlin.requests.responses.NewsResponse
import com.ozgurberat.newsprojectkotlin.requests.responses.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("v2/sources")
    suspend fun getSources(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Response<SourceResponse>
}