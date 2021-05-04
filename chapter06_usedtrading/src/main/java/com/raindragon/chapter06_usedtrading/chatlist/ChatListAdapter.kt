package com.raindragon.chapter06_usedtrading.chatlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raindragon.chapter06_usedtrading.databinding.ItemChatListBinding

// Created by raindragonn on 2021/05/04.

class ChatListAdapter(val onItemClickListener: (ChatListItemModel) -> Unit) :
    ListAdapter<ChatListItemModel, ChatListAdapter.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItemModel>() {
            override fun areItemsTheSame(
                oldItem: ChatListItemModel,
                newItem: ChatListItemModel
            ): Boolean =
                oldItem.key == newItem.key

            override fun areContentsTheSame(
                oldItem: ChatListItemModel,
                newItem: ChatListItemModel
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(item: ChatListItemModel) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener(item)
                }
                tvTitle.text = item.itemTitle
            }
        }
    }
}