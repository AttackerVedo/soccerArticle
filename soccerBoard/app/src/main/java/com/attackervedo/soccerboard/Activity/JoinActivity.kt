package com.attackervedo.soccerboard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.attackervedo.soccerboard.CustomToast
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.MainActivity
import com.attackervedo.soccerboard.R
import com.attackervedo.soccerboard.dataModel.UserData
import com.attackervedo.soccerboard.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityJoinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.joinBackBtn.setOnClickListener {
            backBtn()
        }

        binding.joinLoginBtn.setOnClickListener {
            createUser()
        }

    }//onCreate
    private fun backBtn(){
        val intent = Intent(this@JoinActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }//backBtn

    private fun createUser() {

        // 패턴
        // 이메일 패턴
        var emailPattern = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        // 패스워드 패턴 숫자,문자 포함의 6~12이내
        var passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,12}\$")

        //값가져오기
        val email = binding.joinEmail.text.toString()
        val password = binding.joinPassword.text.toString()
        val passwordCheck = binding.joinPasswordCheck.text.toString()
        val nickname = binding.joinNickname.text.toString()
        var joinCheck = true
        if(email.isEmpty()){
            CustomToast.showToast(this,"이메일을 입력해주세요.")
//            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }else if(password.isEmpty()){
            CustomToast.showToast(this,"비밀번호를 입력해주세요.")
//            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }else if(passwordCheck.isEmpty()){
            CustomToast.showToast(this,"비밀번호 확인란을 입력해주세요.")
//            Toast.makeText(this, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }else if(nickname.isEmpty()){
            CustomToast.showToast(this,"닉네임을 입력해주세요.")
//            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }
        // 여기까지가 비어있는지 확인
        else if (!emailPattern.matcher(email).matches()) {
            CustomToast.showToast(this,"이메일 양식이 올바르지 않습니다.")
//            Toast.makeText(this, "이메일 양식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        } //이메일 유효성
        else if(!passwordPattern.matcher(password).matches()){
            CustomToast.showToast(this,"비밀번호는 숫자,문자 포함의 6~12자리이여야 합니다.")
//            Toast.makeText(this, "비밀번호를 숫자,문자 포함의 6~12이내로 입력해주세요.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }
        else if(!password.equals(passwordCheck)){
            CustomToast.showToast(this,"비밀번호와 비밀번호 확인란이 일치하지 않습니다.")
//            Toast.makeText(this, "비밀번호 입력란과 비밀번호 확인란이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            joinCheck = false
        }

        if(joinCheck){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공
                        CustomToast.showToast(this,"회원가입 되셨습니다.")
//                        Toast.makeText(this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        val uid = user?.uid.toString()
                        val userData = UserData(
                            email,
                            uid,
                            nickname
                        )
                        FBRef.userInfo.child(uid).setValue(userData)


                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 회원가입 실패
                        CustomToast.showToast(this,"이미 사용중인 이메일 입니다.")
//                        Toast.makeText(this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }//creatUser
}//finish