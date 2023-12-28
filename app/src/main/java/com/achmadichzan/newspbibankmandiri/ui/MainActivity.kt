package com.achmadichzan.newspbibankmandiri.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.achmadichzan.newspbibankmandiri.adapter.HeadlineAdapter
import com.achmadichzan.newspbibankmandiri.adapter.NewsAdapter
import com.achmadichzan.newspbibankmandiri.databinding.ActivityMainBinding
import com.achmadichzan.newspbibankmandiri.response.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.response.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.util.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(application))[MainViewModel::class.java]

        binding.rvHeadlines.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mainViewModel.listHeadlines.observe(this) { headline ->
            headline?.let { setHeadlinesData(it) }
        }

        binding.rvNews.layoutManager = LinearLayoutManager(this)

        mainViewModel.listNews.observe(this) { news ->
            news?.let { setNewsData(it) }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setHeadlinesData(headline: List<HeadlineArticleItem?>?) {
        val adapter = HeadlineAdapter()
        adapter.submitList(headline)
        binding.rvHeadlines.adapter = adapter
    }

    private fun setNewsData(news: List<NewsArticleItem?>?) {
        val adapter = NewsAdapter()
        adapter.submitList(news)
        binding.rvNews.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHeadline.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.pbNews.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}