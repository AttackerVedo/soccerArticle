package com.attackervedo.soccerboard

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.attackervedo.soccerboard.Activity.IntroActivity
import com.attackervedo.soccerboard.Activity.MyArticleListActivity
import com.attackervedo.soccerboard.Activity.MyInfoActivity
import com.attackervedo.soccerboard.FB.FBRef
import com.attackervedo.soccerboard.FB.FButils
import com.attackervedo.soccerboard.Fragment.HomeFragment
import com.attackervedo.soccerboard.Fragment.NewsFragment
import com.attackervedo.soccerboard.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    private lateinit var binding : ActivityMainBinding
    lateinit var layout_drawer : DrawerLayout
    var backPressedTime : Long = 0
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (savedInstanceState == null) { // 첫프레그먼트 설정하기
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.mainFragment, homeFragment)
                .commit()
        }

        fragmentMoveBtnSettings()

        layout_drawer = binding.layoutDrawer
        binding.mainIvMenuBook.setOnClickListener {
            layout_drawer.openDrawer(GravityCompat.START)
        }

        binding.sideBar.setNavigationItemSelectedListener(this) //네비게이션 메뉴 아이템에 클릭 속성 부여

    }//onCreate

    private fun fragmentMoveBtnSettings() {
        binding.mainHomeBtn.setOnClickListener {
            val homeFragmentTransaction = supportFragmentManager.beginTransaction()
            homeFragmentTransaction.replace(R.id.mainFragment, HomeFragment())
            homeFragmentTransaction.commit()
            binding.mainStatusArticle.setBackgroundColor(ContextCompat.getColor(this,R.color.black))
            binding.mainStatusNews.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

        }// 게시판(홈)눌렀을때

        binding.mainNewBtn.setOnClickListener {
            val newsFragmentTransaction = supportFragmentManager.beginTransaction()
            newsFragmentTransaction.replace(R.id.mainFragment, NewsFragment())
            newsFragmentTransaction.commit()
            binding.mainStatusArticle.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            binding.mainStatusNews.setBackgroundColor(ContextCompat.getColor(this,R.color.black))
        }// 뉴스(링크)눌렀을떄
        binding.mainYoutubeBtn.setOnClickListener {
            showDialog()
        }// 영상(유튜브)눌렀을때

    }

    //백버튼 설정
    override fun onBackPressed() {
        if (layout_drawer.isDrawerOpen(GravityCompat.START)) {
            layout_drawer.closeDrawers()
        } else {
            if (System.currentTimeMillis() - backPressedTime >= 2500) {
                backPressedTime = System.currentTimeMillis()
                CustomToast.showToast(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.")
            } else {
                super.onBackPressed() // 앱 종료
            }
        }
    }

    public fun showDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("화면 이동")
        dialogBuilder.setMessage("youtube로 이어집니다. 이동하시겠습니까?")

        dialogBuilder.setPositiveButton("네") { dialog: DialogInterface, which: Int ->
            // "네" 버튼을 클릭한 경우 처리할 내용
            val youtubeUri = Uri.parse("https://www.youtube.com")
            val intent = Intent(Intent.ACTION_VIEW, youtubeUri)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
            // "아니요" 버튼을 클릭한 경우 처리할 내용
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }//showDialog

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sideBarMyinfo ->{
                val intent = Intent(this@MainActivity, MyInfoActivity::class.java)
                startActivity(intent)
            }
            R.id.sideBarMyArticle ->{
                val intent = Intent(this@MainActivity, MyArticleListActivity::class.java)
                startActivity(intent)
            }

            R.id.sideBarUserOut ->{
                UserOut()
            }
            R.id.sideBarLogout ->{ logout() }
            else ->{}
        }
        layout_drawer.closeDrawers()
        return false
    }

    private fun UserOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>회원탈퇴 안내문</font>"))
        dialogBuilder.setMessage(Html.fromHtml("<font color='#FF0000'>정말로 회원탈퇴 하시겠습니까?</font>"))

        dialogBuilder.setPositiveButton("네") { dialog: DialogInterface, which: Int ->
            // "네" 버튼을 클릭한 경우 처리할 내용
            val currentUser = auth.currentUser
            if (currentUser != null) {
                currentUser.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 회원 탈퇴 성공
                            val uid = currentUser.uid
                            FBRef.userInfoRef.child(uid).removeValue()
                            CustomToast.showToast(this, "탈퇴되었습니다.")
                            finish() // 현재 액티비티 종료
                        } else {
                            // 회원 탈퇴 실패
                            val errorMessage = task.exception?.message
                            Log.d("errorMessage", errorMessage.toString())
                            CustomToast.showToast(this, errorMessage.toString())
                        }
                    }
            }
        }

        dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
            // "아니요" 버튼을 클릭한 경우 처리할 내용
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun logout() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Logout 안내문")
        dialogBuilder.setMessage("로그아웃 하시겠습니까?")

        dialogBuilder.setPositiveButton("네") { dialog: DialogInterface, which: Int ->
            // "네" 버튼을 클릭한 경우 처리할 내용
        auth.signOut()
        val intent = Intent(this@MainActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
        }
        dialogBuilder.setNegativeButton("아니요") { dialog: DialogInterface, which: Int ->
            // "아니요" 버튼을 클릭한 경우 처리할 내용
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


}//finish


