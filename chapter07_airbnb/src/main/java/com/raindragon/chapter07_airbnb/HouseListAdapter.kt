package com.raindragon.chapter07_airbnb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.raindragon.chapter07_airbnb.databinding.ItemHouseListBinding
import com.raindragon.chapter07_airbnb.databinding.ItemHouseViewpagerBinding
import com.raindragon.chapter07_airbnb.model.House
import com.raindragon.chapter07_airbnb.util.dpToPx

// Created by raindragonn on 2021/05/10.

class HouseListAdapter : ListAdapter<House, HouseListAdapter.ViewHolder>(differ) {
    companion object {
        val differ = object : DiffUtil.ItemCallback<House>() {
            override fun areItemsTheSame(oldItem: House, newItem: House): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: House, newItem: House): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemHouseListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemHouseListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: House) {
            binding.apply {
                tvTitle.text = model.title
                tvPrice.text = model.price

                // 일반적인 이미지뷰의 스케일타입이랑 다른 결과를 보일수 있다.
                // transform 의 경우 이미지를 먼저 스케일타입으로 바꾸고 넣어준다

                Glide.with(binding.ivThumbnail)
                    .load(model.imgUrl)
                    .transform(CenterCrop(), RoundedCorners(ivThumbnail.context.dpToPx(12)))
                    .into(ivThumbnail)
            }
        }
    }
}