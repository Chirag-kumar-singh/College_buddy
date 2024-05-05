package com.example.college_buddy.Activities

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.adapters.SecretsAdapter
import com.example.college_buddy.models.Confessions
import com.example.college_buddy.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class Secrets_feeds : BaseActivity() {

    private lateinit var mUserName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secrets_feeds)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }


        var bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.secretsFeed
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                // Add other cases for additional menu items here
                else -> {
                    false
                }
            }
        }

        var buttonWrite = findViewById<Button>(R.id.ButtonWrite)
        buttonWrite.setOnClickListener {
            val intent = Intent(this, Secrets_write::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivity(intent)
        }

//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().getConfessionsList(this)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getConfessionsList(this)
    }

    fun populateConfessionsListToUI(confessionlist: MutableList<Confessions>){
        val secretRecyclerView = findViewById<RecyclerView>(R.id.secretsRecyclerView)
        secretRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = SecretsAdapter(confessionlist)
        secretRecyclerView.adapter = adapter

        // Scroll to the last item in the RecyclerView
        secretRecyclerView.scrollToPosition(adapter.itemCount - 1)
    }

}