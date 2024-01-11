package com.achmadichzan.newspbibankmandiri.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.adapter.HeadlineAdapter
import com.achmadichzan.newspbibankmandiri.adapter.NewsAdapter
import com.achmadichzan.newspbibankmandiri.databinding.FragmentHomeBinding
import com.achmadichzan.newspbibankmandiri.model.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.model.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailFragment
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailNewsActivity
import com.achmadichzan.newspbibankmandiri.util.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HeadlineAdapter.OnHeadlineClickListener, NewsAdapter.OnNewsClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(requireActivity().application)
    }
    private lateinit var swipeRefresh: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lifecycleScope = viewLifecycleOwner.lifecycleScope

        swipeRefresh = binding?.swipeRefresh!!

        swipeRefresh.setOnRefreshListener {
            fetchData()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED){
                    delay(1000)
                    swipeRefresh.isRefreshing = false
                }
            }
        }

        fetchData()

        binding?.rvHeadlines?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        lifecycleScope.launch{
            homeViewModel.listHeadlines.collect { headline ->
                headline?.let { setHeadlinesData(it) }
            }
        }

        binding?.rvNews?.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch{
            homeViewModel.listNews.collect { news ->
                news?.let { setNewsData(it) }
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setHeadlinesData(headline: List<HeadlineArticleItem?>?) {
        val adapter = HeadlineAdapter(this)
        adapter.submitList(headline)
        binding?.rvHeadlines?.adapter = adapter
    }

    private fun setNewsData(news: List<NewsArticleItem?>?) {
        val adapter = NewsAdapter(this)
        adapter.submitList(news)
        binding?.rvNews?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbHeadline?.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding?.pbNews?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun fetchData() {
        homeViewModel.refreshData()
    }

    override fun onHeadlineClicked(user: HeadlineArticleItem) {
        val detailFragment = DetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(DetailFragment.CONTENT, user)
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.home_activity, detailFragment, DetailFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    override fun onNewsClicked(user: NewsArticleItem) {
        val intent = Intent(requireContext(), DetailNewsActivity::class.java)
        intent.putExtra(DetailNewsActivity.CONTENT, user)
        startActivity(intent)
    }

}