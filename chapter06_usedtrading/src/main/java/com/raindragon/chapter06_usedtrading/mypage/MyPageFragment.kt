package com.raindragon.chapter06_usedtrading.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter06_usedtrading.R
import com.raindragon.chapter06_usedtrading.databinding.FragmentMypageBinding

// Created by raindragonn on 2021/05/01.

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        initViews()
    }

    override fun onStart() {
        super.onStart()

        auth.currentUser?.let {
            binding.apply {
                etEmail.setText(it.email)
                etPassword.setText("**********")
                etEmail.isEnabled = false
                etPassword.isEnabled = false
                btnSignInout.text = "로그아웃"
                btnSignInout.isEnabled = true
                btnSignUp.isEnabled = false
            }
        } ?: run {
            binding.apply {
                etEmail.text.clear()
                etEmail.isEnabled = true

                etPassword.text.clear()
                etPassword.isEnabled = true

                btnSignInout.text = "로그인"
                btnSignInout.isEnabled = false
                btnSignUp.isEnabled = false
            }
        }
    }

    private fun initViews() {
        binding.apply {
            btnSignInout.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                auth.currentUser?.let {
                    signOut()
                } ?: run {
                    signIn(email, password,
                        successListener = {
                            successSignIn()
                        }, failListener = {
                            Toast.makeText(
                                requireContext(),
                                "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                }
            }

            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            etEmail.addTextChangedListener {
                val enable = etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()
                btnSignInout.isEnabled = enable
                btnSignUp.isEnabled = enable
            }

            etPassword.addTextChangedListener {
                val enable = etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()
                btnSignInout.isEnabled = enable
                btnSignUp.isEnabled = enable
            }
        }
    }

    private fun signIn(
        email: String,
        password: String,
        successListener: () -> Unit,
        failListener: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successListener()
                } else {
                    failListener()
                }
            }
    }

    private fun signOut() {
        auth.signOut()
        binding.apply {
            etEmail.text.clear()
            etEmail.isEnabled = true
            etPassword.text.clear()
            etPassword.isEnabled = true

            btnSignInout.text = "로그인"
            btnSignInout.isEnabled = false
            btnSignUp.isEnabled = false
        }
    }

    private fun successSignIn() {
        auth.currentUser ?: kotlin.run {
            Toast.makeText(requireContext(), "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        binding.apply {
            etEmail.isEnabled = false
            etPassword.isEnabled = false
            btnSignUp.isEnabled = false
            btnSignInout.text = "로그아웃"
        }
    }
}