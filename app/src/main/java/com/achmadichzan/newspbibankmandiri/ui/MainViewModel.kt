package com.achmadichzan.newspbibankmandiri.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.newspbibankmandiri.response.ArticlesItem
import com.achmadichzan.newspbibankmandiri.response.ArticlesItems
import com.achmadichzan.newspbibankmandiri.response.HeadlineResponse
import com.achmadichzan.newspbibankmandiri.response.NewsResponse
import com.achmadichzan.newspbibankmandiri.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainViewModel(private val appContext: Context): ViewModel() {

    private val _listNews = MutableLiveData<List<ArticlesItem?>?>()
    val listNews: LiveData<List<ArticlesItem?>?> get() = _listNews

    private val _listHeadlines = MutableLiveData<List<ArticlesItems?>?>()
    val listHeadlines: LiveData<List<ArticlesItems?>?> get() = _listHeadlines

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getNews("Indonesia")
        getHeadlines("id")
    }

    private fun getNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.DAY_OF_MONTH, -1)

            val formattedDate = SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault()
            ).format(currentDate.time)

            val client = ApiConfig.getApiService(appContext).getNews(
                query = query,
                fromDate = formattedDate,
                sortBy = "publishedAt"
            )
            Log.i(TAG, "getDate: $formattedDate")

            client.enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listNews.value = response.body()?.articles
                        Log.i(TAG, "onResponse: getNews Succeed")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "Failed to get users: ${t.message}")
                }
            })
        }
    }

    private fun getHeadlines(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = ApiConfig.getApiService(appContext).getHeadlines(
                country = query,
                category = "business"
            )
            client.enqueue(object : Callback<HeadlineResponse> {
                override fun onResponse(
                    call: Call<HeadlineResponse>,
                    response: Response<HeadlineResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listHeadlines.value = response.body()?.articles
                        Log.i(TAG, "onResponse: getHeadlines Succeed")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<HeadlineResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "Failed to get users: ${t.message}")
                }
            })
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
