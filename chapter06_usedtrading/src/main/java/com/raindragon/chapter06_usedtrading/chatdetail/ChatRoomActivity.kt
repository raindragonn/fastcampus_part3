package com.raindragon.chapter06_usedtrading.chatdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter06_usedtrading.DBKey
import com.raindragon.chapter06_usedtrading.chatlist.ChatListFragment
import com.raindragon.chapter06_usedtrading.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var chatDB: DatabaseReference

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val chatList = mutableListOf<ChatItemModel>()
    private val chatItemAdapter by lazy { ChatItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatKey = intent.getLongExtra(ChatListFragment.EXTRA_CHAT_KEY, -1).toString()

        chatDB = Firebase.database.reference.child(DBKey.DB_CHAT)
            .child(chatKey)

        chatDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItemModel::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                chatItemAdapter.submitList(chatList)
                chatItemAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        initViews()
    }

    private fun initViews() {
        binding.apply {
            rvMessage.adapter = chatItemAdapter
            rvMessage.layoutManager = LinearLayoutManager(this@ChatRoomActivity)

            btnSend.setOnClickListener {
                val chatItem = ChatItemModel(
                    senderId = auth.currentUser.uid,
                    message = etMessage.text.toString()
                )
                send(chatItem)
            }
        }

    }

    private fun send(chatItem: ChatItemModel) {
        chatDB.push().setValue(chatItem)
    }
}