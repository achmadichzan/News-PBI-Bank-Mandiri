package com.achmadichzan.newspbibankmandiri.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.adapter.HeadlineAdapter
import com.achmadichzan.newspbibankmandiri.adapter.NewsAdapter
import com.achmadichzan.newspbibankmandiri.databinding.ActivityMainBinding
import com.achmadichzan.newspbibankmandiri.data.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.data.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailFragment
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailNewsActivity
import com.achmadichzan.newspbibankmandiri.util.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), HeadlineAdapter.OnHeadlineClickListener, NewsAdapter.OnNewsClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefresh = binding.swipeRefresh

        swipeRefresh.setOnRefreshListener {
            fetchData()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED){
                    delay(1000)
                    swipeRefresh.isRefreshing = false
                }
            }
        }

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application)
        )[MainViewModel::class.java]

        fetchData()

        binding.rvHeadlines.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.listHeadlines.collect { headline ->
                    headline?.let { setHeadlinesData(it) }
                }
            }
        }

        binding.rvNews.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.listNews.collect { news ->
                    news?.let { setNewsData(it) }
                }
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.isLoading.collect {
                    showLoading(it)
                }
            }
        }

    }

    private fun setHeadlinesData(headline: List<HeadlineArticleItem?>?) {
        val adapter = HeadlineAdapter(this)
        adapter.submitList(headline)
        binding.rvHeadlines.adapter = adapter
    }

    private fun setNewsData(news: List<NewsArticleItem?>?) {
        val adapter = NewsAdapter(this)
        adapter.submitList(news)
        binding.rvNews.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHeadline.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.pbNews.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun fetchData() {
        mainViewModel.refreshData()
    }

    override fun onHeadlineClicked(user: HeadlineArticleItem) {
        val detailFragment = DetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(DetailFragment.CONTENT, user)
        detailFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity, detailFragment, DetailFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewsClicked(user: NewsArticleItem) {
        val intent = Intent(applicationContext, DetailNewsActivity::class.java)
        intent.putExtra(DetailNewsActivity.CONTENT, user)
        startActivity(intent)
    }
}