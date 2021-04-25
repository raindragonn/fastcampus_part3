package com.raindragon.chapter04_bookreview.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raindragon.chapter04_bookreview.databinding.ItemBookBinding
import com.raindragon.chapter04_bookreview.model.BookModel

// Created by raindragonn on 2021/04/24.

class BookAdapter(private val itemCLickListener : (BookModel) -> Unit) : ListAdapter<BookModel, BookAdapter.BookItemViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BookModel>() {

            // 아이템이 같으냐
            override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean =
                oldItem == newItem

            // 아이템의 내용이 같느냐
            override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder =
        BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class BookItemViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookModel) {
            binding.tvTitle.text = book.title
            binding.tvDescription.text = book.description

            binding.root.setOnClickListener {
                itemCLickListener(book)
            }
            Glide.with(binding.ivCover).load(book.coverSmallUrl).into(binding.ivCover)

        }
    }

    // diffUtil = 아이템이 변경에 대한 판단을 도와준다

}