package com.achmadichzan.newspbibankmandiri.ui.detail

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import androidx.appcompat.app.AppCompatActivity
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.data.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.databinding.ActivityDetailNewsBinding
import com.achmadichzan.newspbibankmandiri.util.addIndentation
import com.achmadichzan.newspbibankmandiri.util.parcelable
import com.bumptech.glide.Glide

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsArticleItem = intent.parcelable<NewsArticleItem>(DetailFragment.CONTENT)

        newsArticleItem?.let { setupNewsData(it) }
    }

    private fun setupNewsData(news: NewsArticleItem) {
        binding.apply {
            Glide.with(this@DetailNewsActivity)
                .load(news.urlToImage)
                .error(R.drawable.no_image)
                .into(ivaNews)
            tvaTitle.text = news.title
            tvaDescription.text = addIndentation(news.content ?: "No description")
        }
    }

    companion object {
        const val CONTENT = "content"
    }
}