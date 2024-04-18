package com.example.college_buddy.Activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        // Adding the handler to after the a task after some delay.
        Handler().postDelayed({
            // Here if the user is signed in once and not signed out again from the app. So next time while coming into the app
            // we will redirect him to MainScreen or else to the Intro Screen as it was before.

//            //Get the current user id
            var currentUserId = FirestoreClass().getCurrentUserID()
//
//
            if(currentUserId.isNotEmpty()){
                //start the main activity
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                //start the intro activity
                startActivity(Intent(this, IntroActivity::class.java))
            }
            // Start the Intro Activity
           // startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            finish() // Call this when your activity is done and should be closed.
        }, 2500)
    }


}