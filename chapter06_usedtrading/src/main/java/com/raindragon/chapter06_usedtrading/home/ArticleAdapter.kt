package com.raindragon.chapter06_usedtrading.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raindragon.chapter06_usedtrading.databinding.ItemArticleBinding
import com.raindragon.chapter06_usedtrading.model.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

// Created by raindragonn on 2021/05/01.

class ArticleAdapter(val onItemClickListener: (ArticleModel) -> Unit) :
    ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean =
                oldItem.createdAt == newItem.createdAt

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(item: ArticleModel) {
            binding.apply {
                val format = SimpleDateFormat("MM월 dd일")
                val date = Date(item.createdAt)

                tvTitle.text = item.title
                tvDate.text = format.format(date).toString()
                tvPrice.text = item.price

                if (item.imageUrl.isNotEmpty()) {
                    Glide.with(ivThumbnail)
                        .load(item.imageUrl)
                        .into(ivThumbnail)
                }

                root.setOnClickListener {
                    onItemClickListener(item)
                }
            }
        }
    }
}