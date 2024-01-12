package com.achmadichzan.newspbibankmandiri.state

import com.achmadichzan.newspbibankmandiri.model.HeadlineArticleItem

data class HeadlineViewState(
    val listHeadlines: List<HeadlineArticleItem?>?,
    val isHeadlineLoading: Boolean = false,
    val errorHeadlineText: Boolean = false
)