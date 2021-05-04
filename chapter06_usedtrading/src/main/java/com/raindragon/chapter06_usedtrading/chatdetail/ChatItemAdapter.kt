package com.raindragon.chapter06_usedtrading.chatdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raindragon.chapter06_usedtrading.databinding.ItemChatBinding

// Created by raindragonn on 2021/05/04.

class ChatItemAdapter :
    ListAdapter<ChatItemModel, ChatItemAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItemModel>() {
            override fun areItemsTheSame(
                oldItem: ChatItemModel,
                newItem: ChatItemModel
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ChatItemModel,
                newItem: ChatItemModel
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(item: ChatItemModel) {
            binding.apply {
                tvSender.text = item.senderId
                tvMessage.text = item.message
            }
        }
    }
}