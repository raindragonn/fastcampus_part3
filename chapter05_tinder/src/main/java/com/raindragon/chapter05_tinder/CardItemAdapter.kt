package com.raindragon.chapter05_tinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raindragon.chapter05_tinder.databinding.ItemCardBinding
import com.raindragon.chapter05_tinder.model.CardItem

// Created by raindragonn on 2021/04/30.

class CardItemAdapter : ListAdapter<CardItem, CardItemAdapter.CardViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder =
        CardViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cardItem: CardItem) {
            binding.tvName.text = cardItem.name
        }
    }
}