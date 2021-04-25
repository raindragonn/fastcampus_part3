package com.raindragon.chapter04_bookreview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.raindragon.chapter04_bookreview.adapter.BookAdapter
import com.raindragon.chapter04_bookreview.adapter.HistoryAdapter
import com.raindragon.chapter04_bookreview.databinding.ActivityMainBinding
import com.raindragon.chapter04_bookreview.model.BestSellerDto
import com.raindragon.chapter04_bookreview.model.HistoryEntity
import com.raindragon.chapter04_bookreview.model.SearchBookDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var db: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDataBase.getInstance(applicationContext)

        initViews()
        getBooksData()
    }

    private fun getBooksData() {
        Api.bookService.getBestSellerBooks(getString(R.string.interparAPIKey)).enqueue(
            object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) return

                    response.body()?.let {
                        bookAdapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }


    private fun initViews() {
        bookAdapter = BookAdapter{
            startActivity(Intent(this,DetailActivity::class.java).apply {
                putExtra("book",it)
            })
        }
        historyAdapter = HistoryAdapter {
            deleteSearchKeyword(it)
        }

        binding.apply {
            rvBook.layoutManager = LinearLayoutManager(this@MainActivity)
            rvBook.adapter = bookAdapter

            rvHistory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvHistory.adapter = historyAdapter

            etSearch.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                    event.action == MotionEvent.ACTION_DOWN
                ) {
                    search(binding.etSearch.text.toString())
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            etSearch.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    showHistoryView()
                }
                return@setOnTouchListener false
            }
        }

    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            runOnUiThread {
                showHistoryView()
            }
        }.start()
    }

    private fun search(keyword: String) {
        Api.bookService.getBooksByName(
            getString(R.string.interparAPIKey),
            keyword
        ).enqueue(object : Callback<SearchBookDTO> {
            override fun onResponse(call: Call<SearchBookDTO>, response: Response<SearchBookDTO>) {

                hideHistoryView()
                saveSearchKeyword(keyword)
                if (response.isSuccessful.not()) return
                bookAdapter.submitList(response.body()?.books.orEmpty())
            }

            override fun onFailure(call: Call<SearchBookDTO>, t: Throwable) {

                hideHistoryView()

                t.printStackTrace()
            }
        })
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(HistoryEntity(null, keyword))
        }.start()
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                binding.rvHistory.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
        binding.rvHistory.isVisible = true
    }

    private fun hideHistoryView() {
        binding.rvHistory.isVisible = false
    }
}