package com.achmadichzan.newspbibankmandiri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.databinding.NewsItemBinding
import com.achmadichzan.newspbibankmandiri.data.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.util.formatDate
import com.bumptech.glide.Glide


class NewsAdapter: ListAdapter<NewsArticleItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    class NewsViewHolder(private val newsBinding: NewsItemBinding) : RecyclerView.ViewHolder(newsBinding.root){
        fun bind(news: NewsArticleItem) {
            newsBinding.apply {
                tvTitle.text = news.title
                Glide.with(itemView)
                    .load(news.urlToImage)
                    .error(R.drawable.no_image)
                    .into(ivItem)
                tvAuthor.text = news.author ?: root.context.getString(R.string.unknown)
                val newsDate = news.publishedAt?.let { formatDate(it) }
                tvDate.text = newsDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsArticleItem>() {
            override fun areItemsTheSame(oldItem: NewsArticleItem, newItem: NewsArticleItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: NewsArticleItem, newItem: NewsArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}