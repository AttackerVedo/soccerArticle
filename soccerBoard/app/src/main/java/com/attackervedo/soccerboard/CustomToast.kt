package com.attackervedo.soccerboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class CustomToast {

    companion object {
        @SuppressLint("NonConstantResourceId")
        private val DEFAULT_IMAGE_RES_ID = R.drawable.baseline_sports_soccer_24 // 이미지 리소스 ID

        fun showToast(context: Context, message: String) {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val imageView = layout.findViewById<ImageView>(R.id.toastImage)
            val textView = layout.findViewById<TextView>(R.id.toastText)

            imageView.setImageResource(DEFAULT_IMAGE_RES_ID) // 이미지 리소스 ID 고정
            textView.text = message

            val toast = Toast(context)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        }
    }

}