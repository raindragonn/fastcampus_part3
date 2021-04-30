package com.raindragon.chapter05_tinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.raindragon.chapter05_tinder.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    companion object {

    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth.getInstance() 와 동일
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun initViews() {
        binding.apply {
            etEmail.addTextChangedListener { checkBtnEnabled() }
            etPassword.addTextChangedListener { checkBtnEnabled() }

            btnLogin.setOnClickListener { onLogin() }
            btnSignup.setOnClickListener { onSignUp() }

            // facebook 에서 받아올 정보
            btnFacebookLogin.setPermissions("email", "public_profile")
            btnFacebookLogin.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result ?: return

                        // 로그인 엑세스 토큰을 firebase 에 넘기기
                        val credential =
                            FacebookAuthProvider.getCredential(result.accessToken.token)


                        auth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful)
                                    handleSuccessLogin()
                                else
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "페이스북 로그인이 실패했습니다.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                            }
                    }

                    override fun onCancel() {
                        // 로그인 취소
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(this@LoginActivity, "페이스북 로그인이 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun onLogin() {
        val email = getInputEmail()
        val password = getInputPassword()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    handleSuccessLogin()
                else
                    Toast.makeText(
                        this@LoginActivity,
                        "이메일 또는 비밀번호를 확인해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@LoginActivity,
                    "로그인에 실패 했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun onSignUp() {
        val email = getInputEmail()
        val password = getInputPassword()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    Toast.makeText(this@LoginActivity, "회원가입에 성공 했습니다.", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(
                        this@LoginActivity,
                        "이미 가입한 이메일이거나, 회원가입에 실패했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
            }
    }

    private fun ActivityLoginBinding.checkBtnEnabled() {
        val enable = etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()
        btnLogin.isEnabled = enable
        btnSignup.isEnabled = enable
    }

    private fun getInputEmail() = binding.etEmail.text.toString()
    private fun getInputPassword() = binding.etPassword.text.toString()

    private fun handleSuccessLogin() {
        auth.currentUser?.let {
            val userId = it.uid

            // firebase realtime database 에서 json 형태로 저장된 데이터를 가져오기
            val currentUserDB = Firebase.database.reference.child(DBKey.USERS).child(userId)
            val user = mutableMapOf<String, Any>(DBKey.USER_ID to userId)
            currentUserDB.updateChildren(user)
            finish()
        } ?: run {
            Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

    }
}
