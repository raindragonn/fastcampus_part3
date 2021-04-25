package com.raindragon.chapter04_bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raindragon.chapter04_bookreview.databinding.ItemHistoryBinding
import com.raindragon.chapter04_bookreview.model.HistoryEntity

// Created by raindragonn on 2021/04/25.

class HistoryAdapter(val historyDeleteClickListener: (String) -> Unit) :
    ListAdapter<HistoryEntity, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HistoryEntity>() {

            // 아이템이 같으냐
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean =
                oldItem == newItem

            // 아이템의 내용이 같느냐
            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean =
                oldItem.uid == newItem.uid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder =
        HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            binding.tvKeyword.text = history.keyword
            binding.btnDelete.setOnClickListener {
                historyDeleteClickListener.invoke(history.keyword.orEmpty())
            }
        }
    }

    // diffUtil = 아이템이 변경에 대한 판단을 도와준다
}