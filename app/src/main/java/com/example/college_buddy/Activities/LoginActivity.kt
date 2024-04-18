package com.example.college_buddy.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.college_buddy.Firebase.FirestoreClass

import com.example.college_buddy.R
import com.example.college_buddy.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        val emailedittext = findViewById<EditText>(R.id.emailedittext)
        val passwordedittext = findViewById<EditText>(R.id.passwordedittext)
        val forgotpassword = findViewById<TextView>(R.id.forgotpasswordtextView)


        emailedittext.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Clear the hint text when the EditText gains focus
                emailedittext.hint = ""

            } else {
                // Restore the hint text when the EditText loses focus and is empty
                if (emailedittext.text.isEmpty()) {
                    emailedittext.hint = "Email Address"
                }
            }
        }

        passwordedittext.setOnFocusChangeListener{view, hasFocus ->
            if(hasFocus){
                passwordedittext.hint = ""
            }else{
                if(passwordedittext.text.isEmpty()){
                    passwordedittext.hint = "Password"
                }
            }
        }

        forgotpassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }




    private fun setupActionBar() {

        var toolbar_log_in_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_log_in_activity)
        setSupportActionBar(toolbar_log_in_activity)
        supportActionBar?.title = ""

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back__1__1)
        }

        toolbar_log_in_activity.setNavigationOnClickListener { onBackPressed() }

        // START
        // Click event for sign-up button.

        var btn_sign_up = findViewById<TextView>(R.id.logintextview2)
        btn_sign_up.setOnClickListener{
            signInRegisteredUser()
        }
    }

    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser(){

        val emailededittext = findViewById<EditText>(R.id.emailedittext)
        val passwordedittext = findViewById<EditText>(R.id.passwordedittext)

        val email: String = emailededittext.text.toString().trim {it <= ' '}
        val password: String = passwordedittext.text.toString()

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {

                        val verification = FirebaseAuth.getInstance().currentUser?.isEmailVerified
                        if(verification == true){
                            Toast.makeText(
                                this@LoginActivity,
                                "You have successfully signed in.",
                                Toast.LENGTH_LONG
                            ).show()

                            FirestoreClass().loadUserData(this)
                        }
                        else {
                            showToast("Please verify your email, Check in spam also!")
                        }
                       // Calling the FirestoreClass signInUser function to get the data of user from database.
                        //FireStoreClass().loadUserdata(this@LoginActivity)
                        // END

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }

    private fun validateForm(email: String , password: String,): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showToast("Please enter an email")
                false
            }
//            !email.endsWith("@iiitk.ac.in") -> {
//                showToast("Email must end with @iiitk.ac.in")
//                false
//            }
            TextUtils.isEmpty(password) -> {
                showToast("Please enter a password")
                false
            }
            password.length < 8 -> {
                showToast("Password must be at least 8 characters long")
                false
            }
            else -> true
        }
    }

    fun logInSuccess(user: User){
        hideProgressDialog()
//        if()
        startActivity(Intent(this, MainActivity::class.java))
        showToast("You can move forward")
        finish()
    }
}