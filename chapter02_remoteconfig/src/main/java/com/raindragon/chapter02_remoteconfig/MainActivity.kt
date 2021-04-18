package com.raindragon.chapter02_remoteconfig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.raindragon.chapter02_remoteconfig.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG: String = "dev_log:${MainActivity::class.java.simpleName}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initData()
    }

    private fun initData() {
//        기본적으로 12시간을 간격으로 캐싱한다.
//        때문에 개발용으로 확인할때는 시간을 단축 시켜줘야한다.(배포시는 사용 x)

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )

        // 패치 완료 콜백
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            binding.pbLoading.isVisible = false
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)
            }
        }

    }

    private fun parseQuotesJson(strJson: String): List<QuoteModel> {
        val jsonArr = JSONArray(strJson)
        var jsonList = emptyList<JSONObject>()

        for (index in 0 until jsonArr.length()) {
            val jsonObject = jsonArr.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return jsonList.map {
            QuoteModel(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes: List<QuoteModel>, isNameReveled: Boolean) {
        val adapter = QuotePagerAdapter(quotes, isNameReveled)

        binding.vp.adapter = adapter
        binding.vp.setCurrentItem(adapter.itemCount / 2 - quotes.size/2, false)
    }

    private fun initViews() {
        // page 가 스크롤 될떄 효과주기

        // position 은 정적일때 0을 기준 + - 된다.
        binding.vp.setPageTransformer { page, position ->
            Log.d(TAG, "initViews: position = $position")


            when {
                // position 의 절댓값으로 신경 안쓰는 부분
                position.absoluteValue >= 1.0f -> {
                    page.alpha = 0f
                }

                // 화면 중앙
                position == 0F -> {
                    page.alpha = 1f
                }

                // 화면 전환중
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }

            }
        }
    }
}