package com.raindragon.chapter06_usedtrading.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.raindragon.chapter06_usedtrading.DBKey
import com.raindragon.chapter06_usedtrading.databinding.ActivityAddArticleBinding
import com.raindragon.chapter06_usedtrading.model.ArticleModel

class AddArticleActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_PERMISSION_CODE = 1010
        const val REQUEST_CONTENT_CODE = 2020
    }

    private var selectedUri: Uri? = null

    private lateinit var binding: ActivityAddArticleBinding

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val storage: FirebaseStorage by lazy { Firebase.storage }
    private val articleDB: DatabaseReference by lazy { Firebase.database.reference.child(DBKey.DB_ARTICLES) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            btnImageAdd.setOnClickListener {
                if (checkPermission())
                    startContentProvider()
            }

            btnSubmit.setOnClickListener {
                val sellerId = auth.currentUser?.uid.orEmpty()
                val title = etTitle.text.toString()
                val price = etPrice.text.toString()

                showLoading()

                if (selectedUri != null) {
                    val photoUri = selectedUri ?: return@setOnClickListener
                    uploadPhoto(photoUri,
                        onSuccessListener = { url ->
                            submitData(sellerId, title, price, url)
                        },
                        onErrorListener = {
                            Toast.makeText(
                                this@AddArticleActivity,
                                "사진 업로드에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            hideLoading()
                        })
                } else {
                    submitData(sellerId, title, price, "")
                }
            }
        }
    }

    private fun uploadPhoto(
        photoUri: Uri,
        onSuccessListener: (String) -> Unit,
        onErrorListener: () -> Unit
    ) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child(DBKey.DB_ARTICLE_PHOTO).child(fileName)
            .putFile(photoUri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // 파일 업로드 성공시
                    // 해당 파일의 다운로드 유알엘을가져오기
                    storage.reference.child(DBKey.DB_ARTICLE_PHOTO).child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            onSuccessListener(uri.toString())
                        }.addOnFailureListener {
                            onErrorListener()
                        }
                } else {
                    onErrorListener()
                }
            }
    }

    private fun submitData(sellerId: String, title: String, price: String, imageUri: String) {
        val model = ArticleModel(
            sellerId = sellerId,
            title = title,
            price = price,
            imageUrl = imageUri,
            createdAt = System.currentTimeMillis()
        )

        articleDB.push().setValue(model)
        hideLoading()
        finish()
    }

    private fun checkPermission(): Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                this@AddArticleActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED -> {
                true
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                // 교육용 팝업이 필요한 경우
                showPermissionContextPopUp()
                false
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
                false
            }
        }
    }

    private fun showPermissionContextPopUp() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startContentProvider()
            } else {
                Toast.makeText(this, "권한을 거부 하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CONTENT_CODE) {
            val uri = data?.data
            if (uri != null) {
                binding.ivPhoto.setImageURI(uri)
                selectedUri = uri
            }
        } else {
            Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startContentProvider() {
        startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }, REQUEST_CONTENT_CODE)
    }

    private fun showLoading(){
        binding.pbLoading.isVisible = true
    }
    private fun hideLoading(){
        binding.pbLoading.isVisible = false
    }

}