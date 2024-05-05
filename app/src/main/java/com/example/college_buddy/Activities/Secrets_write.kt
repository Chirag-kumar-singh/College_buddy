package com.example.college_buddy.Activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.Confessions
import com.example.college_buddy.utils.Constants
import java.time.LocalDate
import java.time.LocalTime

class Secrets_write : BaseActivity() {

    private lateinit var mUserName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secrets_write)

        var imageback = findViewById<ImageView>(R.id.imageback)
        imageback.setOnClickListener { finish() }

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        var ButtonConfess = findViewById<Button>(R.id.ButtonConfess)
        var secret = findViewById<EditText>(R.id.secret)
        ButtonConfess.setOnClickListener {
            val secretText = secret.text.toString().trim()

            if(secretText.isNotEmpty()){
                val confession = Confessions(
                    messager = mUserName,
                    message = secretText,
                    timestamp = com.google.firebase.Timestamp.now().toDate().time
                )

                uploadConfession(confession)
            }
        }
    }
    fun uploadConfession(confession: Confessions){
        FirestoreClass().uploadConfession(this, confession)
    }
}