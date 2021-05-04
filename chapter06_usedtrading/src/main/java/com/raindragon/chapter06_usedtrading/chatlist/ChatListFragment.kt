package com.raindragon.chapter06_usedtrading.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter06_usedtrading.DBKey
import com.raindragon.chapter06_usedtrading.R
import com.raindragon.chapter06_usedtrading.chatdetail.ChatRoomActivity
import com.raindragon.chapter06_usedtrading.databinding.FragmentChatBinding

// Created by raindragonn on 2021/05/01.

class ChatListFragment : Fragment(R.layout.fragment_chat) {
    companion object {
        const val EXTRA_CHAT_KEY = "chat_key"
    }

    private lateinit var binding: FragmentChatBinding

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val chatListAdapter: ChatListAdapter by lazy { ChatListAdapter(chatListItemClickListener) }

    private val chatRoomList = mutableListOf<ChatListItemModel>()
    private val chatListItemClickListener: (ChatListItemModel) -> Unit = { model ->
        // 채팅방으로 이동
        context?.let {
            startActivity(Intent(it,ChatRoomActivity::class.java).apply {
                putExtra(EXTRA_CHAT_KEY,model.key)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        chatRoomList.clear()

        initViews()

        auth.currentUser ?: return

        Firebase.database.reference.child(DBKey.DB_USERS).child(auth.currentUser.uid)
            .child(DBKey.DB_CHILD_CHAT).apply {
                addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val model = it.getValue(ChatListItemModel::class.java)
                            model ?: return

                            chatRoomList.add(model)
                        }
                        chatListAdapter.submitList(chatRoomList)
                        chatListAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

    }

    private fun initViews() {
        binding.apply {
            rvChat.adapter = chatListAdapter
            rvChat.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}