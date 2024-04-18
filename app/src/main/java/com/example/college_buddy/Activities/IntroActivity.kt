package com.example.college_buddy.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.college_buddy.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val logintextview = findViewById<TextView>(R.id.logintextview)
        val signuptextview = findViewById<TextView>(R.id.signuptextview)

        logintextview.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signuptextview.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }
}