package com.achmadichzan.newspbibankmandiri.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.newspbibankmandiri.response.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.response.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(private val application: Application): ViewModel() {

    private val _listNews = MutableLiveData<List<NewsArticleItem?>?>()
    val listNews: LiveData<List<NewsArticleItem?>?> get() = _listNews

    private val _listHeadlines = MutableLiveData<List<HeadlineArticleItem?>?>()
    val listHeadlines: LiveData<List<HeadlineArticleItem?>?> get() = _listHeadlines

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
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(currentDate.time)

            try {
                val newsResponse = withContext(Dispatchers.IO) {
                    Log.d(TAG, "getNews: run on thread: ${Thread.currentThread().name}")
                    ApiConfig.getApiService(application).getNews(
                        query = query,
                        fromDate = formattedDate,
                        sortBy = "publishedAt"
                    )
                }
                _isLoading.value = false

                if (newsResponse.newsArticle != null) {
                    _listNews.value = newsResponse.newsArticle
                    Log.d(TAG, "onResponse: getNews Succeed")
                } else {
                    Log.e(TAG, "onFailure: ${newsResponse.status}")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "Failed to get news: ${e.message}")
            }
        }
    }

    private fun getHeadlines(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val headlineResponse = withContext(Dispatchers.IO) {
                    Log.d(TAG, "getHeadlines: run on thread: ${Thread.currentThread().name}")
                    ApiConfig.getApiService(application).getHeadlines(
                        country = query,
                        category = "business"
                    )
                }
                _isLoading.value = false

                if (headlineResponse.headlineArticle != null) {
                    _listHeadlines.value = headlineResponse.headlineArticle
                    Log.d(TAG, "onResponse: getHeadlines Succeed")
                } else {
                    Log.e(TAG, "onFailure: ${headlineResponse.status}")
                }
            } catch (t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Failed to get headlines: ${t.message}")
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
