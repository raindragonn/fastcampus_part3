package com.raindragon.chapter04_bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.bumptech.glide.Glide
import com.raindragon.chapter04_bookreview.databinding.ActivityDetailBinding
import com.raindragon.chapter04_bookreview.databinding.ItemHistoryBinding
import com.raindragon.chapter04_bookreview.model.BookModel
import com.raindragon.chapter04_bookreview.model.ReviewEntity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDataBase.getInstance(applicationContext)

        initViews()
    }

    private fun initViews() {
        val model = intent.getParcelableExtra<BookModel>("book")

        Thread {
            val review: ReviewEntity? = db.reviewDao().getReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.etReview.setText(review?.review.orEmpty())
            }
        }.start()

        binding.apply {
            tvTitle.text = model?.title.orEmpty()
            tvDescription.text = model?.description.orEmpty()
            Glide.with(ivCover)
                .load(model?.coverSmallUrl.orEmpty())
                .into(ivCover)

            btnSave.setOnClickListener {
                Thread {
                    db.reviewDao().saveReview(
                        ReviewEntity(
                            model?.id?.toInt() ?: 0,
                            etReview.text.toString()
                        )
                    )
                }.start()
            }
        }
    }

}