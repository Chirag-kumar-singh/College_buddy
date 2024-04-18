package com.example.college_buddy.Activities

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.college_buddy.R
import com.google.android.material.textfield.TextInputLayout

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)


        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        val editText = findViewById<EditText>(R.id.et_name)

// Set hint text color
        //textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))

// Set box stroke color
        textInputLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.primarycolor)

//// Set end icon
//        val endIconDrawable = ContextCompat.getDrawable(this, R.drawable.ic_che)
//        textInputLayout.endIconDrawable = endIconDrawable
//        textInputLayout.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)))

// Set focus change listener
        editText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Set hint text color when focused
                textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.secondarycolor))
            } else {
                // Restore hint text color when not focused
                textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))
            }
        }

    }
}