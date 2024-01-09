package com.achmadichzan.newspbibankmandiri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achmadichzan.newspbibankmandiri.R
import com.achmadichzan.newspbibankmandiri.databinding.HeadlineItemBinding
import com.achmadichzan.newspbibankmandiri.data.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.util.formatDate
import com.bumptech.glide.Glide

class HeadlineAdapter(private val listener: OnHeadlineClickListener): ListAdapter<HeadlineArticleItem, HeadlineAdapter.HeadlineViewHolder>(DIFF_CALLBACK) {

    inner class HeadlineViewHolder(private val headlineBinding: HeadlineItemBinding) : RecyclerView.ViewHolder(headlineBinding.root){
        fun bind(headline: HeadlineArticleItem, listener: OnHeadlineClickListener) {
            headlineBinding.apply {
                tvHeadline.text = headline.title
                Glide.with(itemView)
                    .load(headline.urlToImage)
                    .error(R.drawable.no_image)
                    .into(ivHeadline)
                hdAuthor.text = headline.author ?: root.context.getString(R.string.unknown)
                hdDate.text = headline.publishedAt?.let { formatDate(it) }
                tvContent.text = headline.content
                itemView.setOnClickListener {
                    listener.onHeadlineClicked(headline)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val headline = getItem(position)
        holder.bind(headline, listener)
    }

    interface OnHeadlineClickListener {
        fun onHeadlineClicked(user: HeadlineArticleItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HeadlineArticleItem>() {
            override fun areItemsTheSame(oldItem: HeadlineArticleItem, newItem: HeadlineArticleItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: HeadlineArticleItem, newItem: HeadlineArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}