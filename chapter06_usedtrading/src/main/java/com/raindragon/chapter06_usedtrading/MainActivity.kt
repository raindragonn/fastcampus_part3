package com.raindragon.chapter06_usedtrading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.raindragon.chapter06_usedtrading.chatlist.ChatListFragment
import com.raindragon.chapter06_usedtrading.databinding.ActivityMainBinding
import com.raindragon.chapter06_usedtrading.home.HomeFragment
import com.raindragon.chapter06_usedtrading.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val chatListFragment: ChatListFragment by lazy { ChatListFragment() }
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        replaceFragment(homeFragment)

        binding.apply {
            bottomNavigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_home -> replaceFragment(homeFragment)
                    R.id.menu_chatList -> replaceFragment(chatListFragment)
                    R.id.menu_myPage -> replaceFragment(myPageFragment)
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
//        activity 상에 attach 된 프래그먼트들을 관리하는 매니저
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fl_container, fragment)
                commit()
            }
    }
}