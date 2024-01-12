package com.achmadichzan.newspbibankmandiri.state

import com.achmadichzan.newspbibankmandiri.model.NewsArticleItem

data class NewsViewState(
    val listNews: List<NewsArticleItem?>?,
    val isNewsLoading: Boolean = false,
    val errorNewsText: Boolean = false
)
