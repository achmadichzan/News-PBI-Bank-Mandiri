package com.achmadichzan.newspbibankmandiri.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.newspbibankmandiri.model.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.model.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(private val application: Application): ViewModel() {

    private val _listNews = MutableStateFlow<List<NewsArticleItem?>?>(null)
    val listNews: StateFlow<List<NewsArticleItem?>?> get() = _listNews

    private val _listHeadlines = MutableStateFlow<List<HeadlineArticleItem?>?>(null)
    val listHeadlines: StateFlow<List<HeadlineArticleItem?>?> get() = _listHeadlines

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun refreshData() {
        getNews("indonesia")
        getHeadlines("us")
    }

    internal fun getNews(query: String) {
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

                if (newsResponse.newsArticle != null) {
                    _listNews.emit(newsResponse.newsArticle)

                    _isLoading.value = false
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

    internal fun getHeadlines(query: String) {
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

                if (headlineResponse.headlineArticle != null) {
                    _listHeadlines.emit(headlineResponse.headlineArticle)

                    _isLoading.value = false
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
