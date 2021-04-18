package com.raindragon.chapter02_remoteconfig

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.raindragon.chapter02_remoteconfig.databinding.ItemQuoteBinding

// Created by raindragonn on 2021/04/18.

// RecyclerView 기반이라 ViewPager2의 어댑터로 가능하다.
class QuotePagerAdapter(
    private val quotes: List<QuoteModel>,
    private val isNameRevealed: Boolean
) : RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder =
        QuoteViewHolder(
            ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size

        holder.bind(quotes[actualPosition], isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE

    class QuoteViewHolder(private val itemBinding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(quote: QuoteModel, isNameRevealed: Boolean) {
            itemBinding.tvQuote.text = "\"${quote.quote}\""
            if (isNameRevealed) {
                itemBinding.tvName.text = "- ${quote.name}"
                itemBinding.tvName.isVisible = true
            } else {
                itemBinding.tvName.isVisible = false
            }
        }
    }
}