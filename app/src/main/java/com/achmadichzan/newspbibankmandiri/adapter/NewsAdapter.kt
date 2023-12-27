package com.achmadichzan.newspbibankmandiri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.adapter.HeadlineAdapter.Companion.DIFF_CALLBACK
import com.achmadichzan.newspbibankmandiri.databinding.NewsItemBinding
import com.achmadichzan.newspbibankmandiri.response.ArticlesItem
import com.bumptech.glide.Glide

class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    class NewsViewHolder(private val newsBinding: NewsItemBinding) : RecyclerView.ViewHolder(newsBinding.root){
        fun bind(news: ArticlesItem) {
            newsBinding.tvTitle.text = news.title
            Glide.with(itemView)
                .load(news.urlToImage)
                .error(R.drawable.no_image)
                .into(newsBinding.ivItem)
            newsBinding.tvAuthor.text = news.author ?: newsBinding.root.context.getString(R.string.unknown)
            newsBinding.tvDate.text = news.publishedAt
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}