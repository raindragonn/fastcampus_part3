package com.raindragon.fastcampus_part3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.raindragon.fastcampus_part3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG: String = "dev_log:${MainActivity::class.java.simpleName}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        updateResult()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.tvFirebaseToken.text = task.result
                    Log.d(TAG, "initFirebase: ${task.result}")
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        binding.tvResult.text =
            (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
                "(으)로 갱신 했습니다."
            } else {
                "(으)로 실행 했습니다."
            }
    }
}