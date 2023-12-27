package com.achmadichzan.newspbibankmandiri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.databinding.HeadlineItemBinding
import com.achmadichzan.newspbibankmandiri.response.ArticlesItems
import com.bumptech.glide.Glide

class HeadlineAdapter: ListAdapter<ArticlesItems, HeadlineAdapter.HeadlineViewHolder>(DIFF_CALLBACK) {

    class HeadlineViewHolder(private val headlineBinding: HeadlineItemBinding) : RecyclerView.ViewHolder(headlineBinding.root){
        fun bind(headline: ArticlesItems) {
            headlineBinding.tvHeadline.text = headline.title
            Glide.with(itemView)
                .load(headline.urlToImage)
                .error(R.drawable.no_image)
                .into(headlineBinding.ivHeadline)
            headlineBinding.hdAuthor.text = headline.author ?: headlineBinding.root.context.getString(R.string.unknown)
            headlineBinding.hdDate.text = headline.publishedAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItems>() {
            override fun areItemsTheSame(oldItem: ArticlesItems, newItem: ArticlesItems): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ArticlesItems, newItem: ArticlesItems): Boolean {
                return oldItem == newItem
            }
        }
    }
}