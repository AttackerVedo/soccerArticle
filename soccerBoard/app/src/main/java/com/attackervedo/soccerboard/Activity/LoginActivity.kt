package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.loginBackBtn.setOnClickListener {
            backBtn()
        }

        binding.loginLoginBtn.setOnClickListener {
            login()
        }


    }//onCreate

    private fun login() {
        val email = binding.loginEmail
        val password = binding.loginPassword

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    CustomToast.showToast(this,"로그인 되었습니다.")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // 로그인 실패
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // 이메일이 없을 때
                        CustomToast.showToast(baseContext,"존재 하지 않는 아이디 입니다. 회원가입을 해주세요.")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // 비밀번호가 틀렸을 때
                         CustomToast.showToast(baseContext,"비밀번호가 틀렸습니다. 다시한번 입력해 주세요.")
                    }
                }
            }


    }

    private fun backBtn(){
        val intent = Intent(this@LoginActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }//backBtn


}//finish