package com.raindragon.chapter07_airbnb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raindragon.chapter07_airbnb.databinding.ItemHouseViewpagerBinding
import com.raindragon.chapter07_airbnb.model.House

// Created by raindragonn on 2021/05/10.

class HouseViewPagerAdapter(val itemClickListener: (House) -> Unit) :
    ListAdapter<House, HouseViewPagerAdapter.ViewHolder>(differ) {
    companion object {
        val differ = object : DiffUtil.ItemCallback<House>() {
            override fun areItemsTheSame(oldItem: House, newItem: House): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: House, newItem: House): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemHouseViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemHouseViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener(currentList[adapterPosition])
            }
        }

        fun bind(model: House) {
            binding.apply {
                tvTitle.text = model.title
                tvPrice.text = model.price
                Glide.with(ivThumbnail).load(model.imgUrl).into(ivThumbnail)
            }
        }

    }
}