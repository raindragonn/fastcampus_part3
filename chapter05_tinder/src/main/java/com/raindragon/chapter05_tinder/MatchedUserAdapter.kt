package com.raindragon.chapter05_tinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raindragon.chapter05_tinder.databinding.ItemCardBinding
import com.raindragon.chapter05_tinder.databinding.ItemMatchedUserBinding
import com.raindragon.chapter05_tinder.model.CardItem

// Created by raindragonn on 2021/04/30.

class MatchedUserAdapter : ListAdapter<CardItem, MatchedUserAdapter.MatchViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder =
        MatchViewHolder(
            ItemMatchedUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MatchViewHolder(private val binding: ItemMatchedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cardItem: CardItem) {
            binding.tvUserName.text = cardItem.name
        }
    }
}