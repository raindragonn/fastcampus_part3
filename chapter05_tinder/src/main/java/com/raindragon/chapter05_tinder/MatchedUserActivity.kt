package com.raindragon.chapter05_tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter05_tinder.databinding.ActivityMatchedUserBinding
import com.raindragon.chapter05_tinder.model.CardItem

class MatchedUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchedUserBinding
    private lateinit var usersDB: DatabaseReference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val matchedAdapter = MatchedUserAdapter()
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersDB = Firebase.database.reference.child(DBKey.USERS)
        initViews()
        getMatchUsers()
    }

    private fun initViews() {
        binding.apply {
            rv.layoutManager = LinearLayoutManager(this@MatchedUserActivity)
            rv.adapter = matchedAdapter
        }
    }

    private fun getMatchUsers() {
        usersDB.child(getCurrentUserId()).child(DBKey.LIKED_BY).child("match").apply {
            addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.key?.isNotEmpty() == true) {
                        getUserByKey(snapshot.key.orEmpty())
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }

    }

    private fun getUserByKey(userId: String) {
        usersDB.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cardItems.add(CardItem(userId, snapshot.child(DBKey.NAME).value.toString()))
                    matchedAdapter.submitList(cardItems)
                    matchedAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getCurrentUserId(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }

}