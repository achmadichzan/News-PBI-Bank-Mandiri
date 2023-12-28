package com.achmadichzan.newspbibankmandiri.retrofit

import com.achmadichzan.newspbibankmandiri.response.HeadlineResponse
import com.achmadichzan.newspbibankmandiri.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String,
        @Query("category") category: String
    ): HeadlineResponse
}
