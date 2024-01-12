package com.achmadichzan.newspbibankmandiri.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadichzan.newspbibankmandiri.retrofit.ApiConfig
import com.achmadichzan.newspbibankmandiri.state.HeadlineViewState
import com.achmadichzan.newspbibankmandiri.state.NewsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeViewModel(private val application: Application): ViewModel() {

    private val _newsState = MutableStateFlow(NewsViewState(null))
    val newsState: StateFlow<NewsViewState> get() = _newsState

    private val _headlineState = MutableStateFlow(HeadlineViewState(null))
    val headlineState: StateFlow<HeadlineViewState> get() = _headlineState

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
            _newsState.value = _newsState.value.copy(
                isNewsLoading = true,
                errorNewsText = false
            )
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
                    _newsState.value = _newsState.value.copy(
                        listNews = newsResponse.newsArticle,
                        isNewsLoading = false
                    )
                    delay(1000)
                    Log.d(TAG, "onResponse: getNews Succeed")
                } else {
                    _newsState.value = _newsState.value.copy(
                        isNewsLoading = false,
                        errorNewsText = true
                    )
                    Log.e(TAG, "onFailure: ${newsResponse.status}")
                }
            } catch (e: Exception) {
                _newsState.value = _newsState.value.copy(
                    isNewsLoading = false,
                    errorNewsText = true
                )
                Log.e(TAG, "Failed to get news: ${e.message}")
            }
        }
    }

    internal fun getHeadlines(query: String) {
        viewModelScope.launch {
            _headlineState.value = _headlineState.value.copy(
                isHeadlineLoading = true,
                errorHeadlineText = false
            )
            try {
                val headlineResponse = withContext(Dispatchers.IO) {
                    Log.d(TAG, "getHeadlines: run on thread: ${Thread.currentThread().name}")
                    ApiConfig.getApiService(application).getHeadlines(
                        country = query,
                        category = "business"
                    )
                }

                if (headlineResponse.headlineArticle != null) {
                    _headlineState.value = _headlineState.value.copy(
                        listHeadlines = headlineResponse.headlineArticle,
                        isHeadlineLoading = false
                    )
                    Log.d(TAG, "onResponse: getHeadlines Succeed")
                } else {
                    _headlineState.value = _headlineState.value.copy(
                        isHeadlineLoading = false,
                        errorHeadlineText = true
                    )
                    Log.e(TAG, "onFailure: ${headlineResponse.status}")
                }
            } catch (t: Throwable) {
                _headlineState.value = _headlineState.value.copy(
                    isHeadlineLoading = false,
                    errorHeadlineText = true
                )
                Log.e(TAG, "Failed to get headlines: ${t.message}")
            }
        }
    }


    companion object {
        private const val TAG = "MainViewModel"
    }
}
