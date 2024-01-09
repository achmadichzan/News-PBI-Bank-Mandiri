package com.achmadichzan.newspbibankmandiri.ui.detail

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.data.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.data.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.databinding.FragmentDetailBinding
import com.achmadichzan.newspbibankmandiri.util.parcelable
import com.bumptech.glide.Glide


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val headlineArticleItem = arguments?.parcelable<HeadlineArticleItem>(CONTENT)

        headlineArticleItem?.let { setupHeadlineData(it) }

    }

    private fun setupHeadlineData(news: HeadlineArticleItem) {
        binding.apply {
            Glide.with(this@DetailFragment)
                .load(news.urlToImage)
                .error(R.drawable.no_image)
                .into(ivfNews)
            tvfTitle.text = news.title
            tvfDescription.text = news.content
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CONTENT = "content"
    }
}