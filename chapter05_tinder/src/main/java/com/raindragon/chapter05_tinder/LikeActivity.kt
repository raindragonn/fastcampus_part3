package com.raindragon.chapter05_tinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter05_tinder.databinding.ActivityLikeBinding
import com.raindragon.chapter05_tinder.model.CardItem
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class LikeActivity : AppCompatActivity(), CardStackListener {

    private lateinit var binding: ActivityLikeBinding
    private lateinit var usersDB: DatabaseReference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val cardAdapter: CardItemAdapter by lazy { CardItemAdapter() }
    private val cardItems = mutableListOf<CardItem>()

    private val cardManager: CardStackLayoutManager by lazy { CardStackLayoutManager(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usersDB = Firebase.database.reference.child(DBKey.USERS)
        getCurrentUser()
        initViews()
        initCardStackView()
    }

    private fun initViews() {
        binding.apply {
            btnLogOut.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@LikeActivity, MainActivity::class.java))
                finish()
            }

            btnMatchList.setOnClickListener {
                startActivity(Intent(this@LikeActivity, MatchedUserActivity::class.java))
            }
        }
    }

    private fun initCardStackView() {
        binding.viewCardStack.apply {
            layoutManager = cardManager
            adapter = cardAdapter
        }
    }

    private fun getCurrentUser() {
        val currentUserDB = usersDB.child(getCurrentUserId())
        // db 에서 일회성으로 값을 가져오기
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(DBKey.NAME).value == null) {
                    showNameInputPopup()
                    return
                }
                // todo 유저정보를 갱신해라
                getUnselectedUsers()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUnselectedUsers() {
        usersDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child(DBKey.USER_ID).value != getCurrentUserId()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.LIKE).hasChild(getCurrentUserId()).not()
                    && snapshot.child(DBKey.LIKED_BY).child(DBKey.DIS_LIKE).hasChild(getCurrentUserId()).not()
                ) {
                    val userId = snapshot.child(DBKey.USER_ID).value.toString()
                    var name = "undecided"
                    if (snapshot.child(DBKey.NAME).value != null) {
                        name = snapshot.child(DBKey.NAME).value.toString()
                    }
                    cardItems.add(CardItem(userId, name))
                    cardAdapter.submitList(cardItems)
                    cardAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                cardItems.find { it.userId == snapshot.key }?.let {
                    it.name = snapshot.child(DBKey.NAME).value.toString()

                }
                cardAdapter.submitList(cardItems)
                cardAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showNameInputPopup() {
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(30, 0, 30, 0)

        val editText = EditText(this).apply {
            layoutParams = lp
            setLines(1)
            maxLines = 1
        }
        container.addView(editText)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.write_name))
            .setView(container)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    saveUerName(editText.text.toString())
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUerName(name: String) {
        val userId = getCurrentUserId()
        // firebase realtime database 에서 json 형태로 저장된 데이터를 가져오기
        val currentUserDB = usersDB.child(userId)
        val user = mutableMapOf<String, Any>(DBKey.USER_ID to userId, DBKey.NAME to name)
        currentUserDB.updateChildren(user)

        // todo 유저정보를 가져와라
        getUnselectedUsers()
    }

    private fun getCurrentUserId(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this@LikeActivity, getString(R.string.not_login), Toast.LENGTH_SHORT).show()
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> disLike()
            Direction.Right -> like()
            else -> {
            }
        }
    }

    private fun like() {

        // 해당 라이브러리는 1부터 시작이라 하나 뺴준다

        val card = cardItems[cardManager.topPosition - 1]
        cardItems.removeFirst()

        usersDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.LIKE)
            .child(getCurrentUserId())
            .setValue(true)

        saveMatchIfOtherUserLikedMe(card.userId)

        // todo 매칭이 된 시점을 봐야한다.

        Toast.makeText(this, "${card.name} 을 Like!", Toast.LENGTH_SHORT).show()
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB =
            usersDB.child(getCurrentUserId()).child(DBKey.LIKED_BY).child(DBKey.LIKE).child(otherUserId)

        otherUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    usersDB.child(getCurrentUserId())
                        .child(DBKey.LIKED_BY)
                        .child("match")
                        .child(otherUserId)
                        .setValue(true)

                    usersDB.child(otherUserId)
                        .child(DBKey.LIKED_BY)
                        .child("match")
                        .child(getCurrentUserId())
                        .setValue(true)

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }


    private fun disLike() {
// 해당 라이브러리는 1부터 시작이라 하나 뺴준다

        val card = cardItems[cardManager.topPosition - 1]
        cardItems.removeFirst()

        usersDB.child(card.userId)
            .child(DBKey.LIKED_BY)
            .child(DBKey.DIS_LIKE)
            .child(getCurrentUserId())
            .setValue(true)

        Toast.makeText(this, "${card.name} 을 disLike!", Toast.LENGTH_SHORT).show()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {}
    override fun onCardRewound() {}
    override fun onCardCanceled() {}
    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
}