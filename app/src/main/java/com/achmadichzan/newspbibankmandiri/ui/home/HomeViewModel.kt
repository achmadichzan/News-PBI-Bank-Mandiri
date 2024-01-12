package com.achmadichzan.newspbibankmandiri.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.model.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.model.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(private val application: Application): ViewModel() {

    private val _listNews = MutableStateFlow<List<NewsArticleItem?>?>(null)
    val listNews: StateFlow<List<NewsArticleItem?>?> get() = _listNews

    private val _listHeadlines = MutableStateFlow<List<HeadlineArticleItem?>?>(null)
    val listHeadlines: StateFlow<List<HeadlineArticleItem?>?> get() = _listHeadlines

    private val _isHeadlineLoading = MutableSharedFlow<Boolean>(replay = 1)
    val isHeadlineLoading: SharedFlow<Boolean> get() = _isHeadlineLoading

    private val _isNewsLoading = MutableSharedFlow<Boolean>(replay = 1)
    val isNewsLoading: SharedFlow<Boolean> get() = _isNewsLoading

    private val _errorHeadlineText = MutableSharedFlow<Boolean>(replay = 1)
    val errorHeadlineText: SharedFlow<Boolean> get() = _errorHeadlineText

    private val _errorNewsText = MutableSharedFlow<Boolean>(replay = 1)
    val errorNewsText: SharedFlow<Boolean> get() = _errorNewsText

    fun fetchData() {
        getNews("indonesia")
        getHeadlines("us")
    }

    init {
        getNews("indonesia")
        getHeadlines("us")
    }

    internal fun getNews(query: String) {
        viewModelScope.launch {
            _isNewsLoading.emit(true)
            _errorNewsText.emit(false)
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
                    _listNews.value = newsResponse.newsArticle
                    delay(1000)
                    _isNewsLoading.emit(false)
                    Log.d(TAG, "onResponse: getNews Succeed")
                } else {
                    _isNewsLoading.emit(false)
                    _errorNewsText.emit(true)
                    Log.e(TAG, "onFailure: ${newsResponse.status}")
                }
            } catch (e: Exception) {
                _isNewsLoading.emit(false)
                _errorNewsText.emit(true)
                Log.e(TAG, "Failed to get news: ${e.message}")
            }
        }
    }

    internal fun getHeadlines(query: String) {
        viewModelScope.launch {
            _isHeadlineLoading.emit(true)
            _errorHeadlineText.emit(false)
            try {
                val headlineResponse = withContext(Dispatchers.IO) {
                    Log.d(TAG, "getHeadlines: run on thread: ${Thread.currentThread().name}")
                    ApiConfig.getApiService(application).getHeadlines(
                        country = query,
                        category = "business"
                    )
                }

                if (headlineResponse.headlineArticle != null) {
                    _listHeadlines.value = headlineResponse.headlineArticle

                    _isHeadlineLoading.emit(false)
                    Log.d(TAG, "onResponse: getHeadlines Succeed")
                } else {
                    _isHeadlineLoading.emit(false)
                    _errorHeadlineText.emit(true)
                    Log.e(TAG, "onFailure: ${headlineResponse.status}")
                }
            } catch (t: Throwable) {
                _isHeadlineLoading.emit(false)
                _errorHeadlineText.emit(true)
                Log.e(TAG, "Failed to get headlines: ${t.message}")
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
