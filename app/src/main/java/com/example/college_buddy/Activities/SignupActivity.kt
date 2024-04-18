package com.example.college_buddy.Activities

import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val nameedittext = findViewById<EditText>(R.id.nameedittext)
        val emailededittext2 = findViewById<EditText>(R.id.emailedittext2)
        val passwordedittext2 = findViewById<EditText>(R.id.passwordedittext2)
        val passwordedittext3 = findViewById<EditText>(R.id.passwordedittext3)

        nameedittext.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                nameedittext.hint = ""
            }else nameedittext.hint = "Full Name"
        }

        emailededittext2.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                emailededittext2.hint = ""
            } else {
                emailededittext2.hint = "Email Address"
            }
        }

        passwordedittext2.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                passwordedittext2.hint = ""
            } else {
                passwordedittext2.hint = "Password"
            }
        }

        passwordedittext3.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                passwordedittext3.hint = ""
            } else {
                passwordedittext3.hint = "Re-enter Password"
            }
        }

        setupActionBar()
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this, "Your have been successfully registered", Toast.LENGTH_SHORT).show()
        hideProgressDialog()

        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun setupActionBar() {

        var toolbar_sign_up_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar_sign_up_activity)
        supportActionBar?.title = ""

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back__1__1)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }


        // Click event for sign-up button.
        var btn_sign_up = findViewById<TextView>(R.id.Signuptextview)
        btn_sign_up.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser() {

        val nameedittext = findViewById<EditText>(R.id.nameedittext)
        val emailededittext2 = findViewById<EditText>(R.id.emailedittext2)
        val passwordedittext2 = findViewById<EditText>(R.id.passwordedittext2)
        val passwordedittext3 = findViewById<EditText>(R.id.passwordedittext3)

        val name: String = nameedittext.text.toString().trim() { it <= ' ' }
        val email: String = emailededittext2.text.toString().trim() { it <= ' ' }
        val password: String = passwordedittext2.text.toString()
        val repassword: String = passwordedittext3.text.toString()

        if (validateForm(name, email, password, repassword)) {
            Toast.makeText(
                this@SignupActivity,
                "Now we can register a new user",
                Toast.LENGTH_SHORT
            ).show()

            val emailParts = email.split("@")[0].split(".")
            val batch = emailParts[0].substring(0, 3).toLongOrNull() ?: 0
            val branch = when (emailParts[0].substring(3, 5)) {
                "cs" -> "Computer Science"
                "ec" -> "Electronics and Communication"
                "ai" -> "Artificial and Data Science"
                "me" -> "Mechanical"
                else -> ""
            }
            val rollNumber = email.split("@")[0]
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    //hide the progess dialog
                    hideProgressDialog()
//
//                //if registration is successfully done
                    if (task.isSuccessful) {

                        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                //Firebase registered user
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                //Registered Email
                                val registeredEmail = firebaseUser.email!!
                                val user = User(firebaseUser.uid, name, registeredEmail, batch, branch, rollNumber)
                                FirestoreClass().registerUser(this, user)
                                Toast.makeText(
                                    this,
                                    "Please verify your email and then try to Login", Toast.LENGTH_SHORT
                                ).show()
                            }
                            ?.addOnFailureListener{
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(
                            this,
                            "Registration failed", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String , password: String, repassword: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showToast("Please enter a name")
                false
            }
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
            password != repassword -> {
                showToast("Passwords do not match")
                false
            }
            else -> true
        }
    }


}