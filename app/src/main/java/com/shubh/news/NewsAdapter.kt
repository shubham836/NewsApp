package com.shubh.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubh.news.databinding.NewsItemBinding
import com.shubh.news.model.Article

class NewsAdapter(
    private val context: Context,
    private val newsList: List<Article>,
    val onClick: (position: Int) -> Unit,
    val onLongPress: ((position: Int) -> Unit)? = null
) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(newsList[position].image).error(R.drawable.error)
            .into(holder.binding.newsImageView)
        holder.binding.titleTextView.text = newsList[position].title
        holder.binding.descriptionTextView.text = newsList[position].description
        holder.binding.publishedDateTextView.text = newsList[position].publishedAt
        holder.binding.newsItemCardLayout.setOnClickListener {
            onClick(position)
        }
        holder.binding.newsItemCardLayout.setOnLongClickListener {
            if (onLongPress!= null){
                onLongPress?.invoke(position)
                true
            }else {
                false
            }
        }
    }
}