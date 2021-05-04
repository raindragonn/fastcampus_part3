package com.raindragon.chapter06_usedtrading.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter06_usedtrading.DBKey
import com.raindragon.chapter06_usedtrading.R
import com.raindragon.chapter06_usedtrading.chatlist.ChatListItemModel
import com.raindragon.chapter06_usedtrading.databinding.FragmentHomeBinding
import com.raindragon.chapter06_usedtrading.model.ArticleModel

// Created by raindragonn on 2021/05/01.

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val TAG: String = "dev_log:${HomeFragment::class.java.simpleName}"

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userDB: DatabaseReference
    private lateinit var articleDB: DatabaseReference

    private val articleAdapter: ArticleAdapter by lazy { ArticleAdapter(articleItemClickListener) }
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val articleItemClickListener: (ArticleModel) -> Unit = { item ->
        if (auth.currentUser != null) {
            //로그인을 한 상태
            if (auth.currentUser?.uid != item.sellerId) {
                val chatRoom = ChatListItemModel(
                    buyerId = auth.currentUser.uid,
                    sellerId = item.sellerId,
                    itemTitle = item.title,
                    key = System.currentTimeMillis()
                )

                userDB.child(auth.currentUser.uid)
                    .child(DBKey.DB_CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                userDB.child(item.sellerId)
                    .child(DBKey.DB_CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                view?.let { Snackbar.make(it, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show() }

            } else {
                // 본인이 올린 아이템
                view?.let { Snackbar.make(it, "내가 올린 아이템입니다.", Snackbar.LENGTH_LONG).show() }
            }
        } else {
            //로그인을 안한 상태
            view?.let { Snackbar.make(it, "로그인 후 사용해 주세요.", Snackbar.LENGTH_LONG).show() }
        }

    }

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildAdded: ${snapshot.key}")
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            // 매핑 실패시 null
            // 사용자 정의 class 로 매핑하여 가져올때는 해당 클래스에 빈 생성자가 필요로 한다.
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
            articleAdapter.notifyDataSetChanged()
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initViews()

        articleList.clear()
        userDB = Firebase.database.reference.child(DBKey.DB_USERS)
        articleDB = Firebase.database.reference.child(DBKey.DB_ARTICLES)
        articleDB.addChildEventListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
        view
    }

    private fun initViews() {
        binding.apply {
            rvArticle.adapter = articleAdapter
            rvArticle.layoutManager = LinearLayoutManager(requireContext())

            fabAdd.setOnClickListener {
                auth.currentUser ?: kotlin.run {
                    view?.let { view ->
                        Snackbar.make(view, "로그인 후 사용해 주세요.", Snackbar.LENGTH_LONG).show()
                    }

                    return@setOnClickListener
                }
                startActivity(Intent(requireContext(), AddArticleActivity::class.java))

            }
        }
    }
}