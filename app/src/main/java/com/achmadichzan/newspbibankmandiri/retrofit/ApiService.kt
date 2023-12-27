package com.achmadichzan.newspbibankmandiri.retrofit

import com.achmadichzan.newspbibankmandiri.response.HeadlineResponse
import com.achmadichzan.newspbibankmandiri.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    fun getNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
    ): Call<NewsResponse>

    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country: String,
        @Query("category") category: String
    ): Call<HeadlineResponse>
}
