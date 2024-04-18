package com.example.college_buddy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.college_buddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        val emailedittext4 = findViewById<EditText>(R.id.emailedittext4)

        emailedittext4.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Clear the hint text when the EditText gains focus
                emailedittext4.hint = ""

            } else {
                // Restore the hint text when the EditText loses focus and is empty
                if (emailedittext4.text.isEmpty()) {
                    emailedittext4.hint = "Email Address"
                }
            }
        }
    }

    private fun setupActionBar() {

        var toolbar_forgot_password_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_forgot_password_activity)
        setSupportActionBar(toolbar_forgot_password_activity)
        supportActionBar?.title = ""

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back__1__1)
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }

        // START
        // Click event for sign-up button.
        val submit_button = findViewById<TextView>(R.id.submit_button)
        val emailedittext4 = findViewById<EditText>(R.id.emailedittext4)
        submit_button.setOnClickListener{
            val email: String = emailedittext4.text.toString().trim{it <= ' '}
            if(email.isEmpty()){
                showToast("Please enter an email!")
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            showToast("Resent Link sent successfully to your entered email!")
                            finish()
                        }else{
                            Toast.makeText(this@ForgotPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}