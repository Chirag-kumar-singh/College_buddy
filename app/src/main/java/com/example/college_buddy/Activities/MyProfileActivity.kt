package com.example.college_buddy.Activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.User
import com.example.college_buddy.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class MyProfileActivity : BaseActivity() {


    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        FirestoreClass().loadUserData(this)
        designlayout()

        var iv_profile_user_image = findViewById<CircleImageView>(R.id.iv_profile_user_image)

        iv_profile_user_image.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)

            }else{
                /*
                Requests permissions to be granted to this application. These permissions
                must be requested in your manifest, they should not be granted to your app,
                and they should have protection level.
                 */
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        var btn_update = findViewById<Button>(R.id.btn_update)
        btn_update.setOnClickListener {
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }

    }

    private fun updateUserProfileData(){
        var userHashMap = HashMap<String, Any>()

        var anyChangesMade = false
        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMade = true
        }

        var et_name = findViewById<EditText>(R.id.et_name)
        if(et_name.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = et_name.text.toString()
            anyChangesMade  = true
        }


        //Update the data in the database.
        if(anyChangesMade)
            FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    /**
     * A function to upload the selected user image to firebase cloud storage.
     */
    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        //getting the storage reference
        if(mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child("USER_IMAGE" + System.currentTimeMillis()
                    + "." + Constants.getFileExtension(this, mSelectedImageFileUri))

            //adding the file to reference
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapshot ->
                //image upload is success
                Log.i(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                //Get the downloadable url from the task snapshot.
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener{
                        uri->
                    Log.i("Downloadable Image URI", uri.toString())
                    //assign the image url to variable.
                    mProfileImageURL = uri.toString()

                    //call a function to update user details in the database.
                    hideProgressDialog()

                    updateUserProfileData()
                }
            }.addOnFailureListener{
                    exception ->
                Toast.makeText(
                    this@MyProfileActivity,
                    exception.message,
                    Toast.LENGTH_SHORT
                ).show()

                hideProgressDialog()
            }
        }
    }




    /**
     * A function to notify the user profile is updated successfully.
     */
    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)

        //setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {

                //Displaying another toast if permission is not granted.
                Toast.makeText(
                    this,
                    "Oops , you just denied the permission for storage, allow it form settings",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            //The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data

            var iv_profile_user_image = findViewById<CircleImageView>(R.id.iv_profile_user_image)

            try {
                //Load the user image in the ImageView.
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)   //URL of the image
                    .centerCrop()       //Scale type of the image.
                    .placeholder(R.mipmap.profile_user_foreground)   //A default place holder.
                    .into(iv_profile_user_image)   //the view in which the image will be loaded.
            }catch (e: IOException){
                e.printStackTrace()
            }
        }


    }

    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    fun setUserDataInUI(user: User){

        mUserDetails = user

        var iv_user_image = findViewById<CircleImageView>(R.id.iv_profile_user_image)
        var et_name = findViewById<EditText>(R.id.et_name)
        var et_email = findViewById<EditText>(R.id.et_email)
        var et_batch = findViewById<EditText>(R.id.et_batch)
        var et_branch = findViewById<EditText>(R.id.et_branch)
        var et_rollnumber = findViewById<EditText>(R.id.et_rollnumber)

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)   //URL of the image
            .centerCrop()       //Scale type of the image.
            .placeholder(R.mipmap.profile_user_foreground)   //A default place holder.
            .into(iv_user_image)   //the view in which the image will be loaded.

        et_name.setText(user.name)
        et_email.setText(user.email)

        if(user.batch != 0L) {
            et_batch.setText(user.batch.toString())
        }
        if(user.branch != "") {
            et_branch.setText(user.branch)
        }
        if(user.rollno != "") {
            et_rollnumber.setText(user.rollno.toString())
        }
    }


    fun designlayout(){
        val nameTextInputLayout = findViewById<TextInputLayout>(R.id.nameTextInputLayout)
        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.emailTextInputLayout)
        val batchTextInputLayout = findViewById<TextInputLayout>(R.id.batchTextInputLayout)
        val branchTextInputLayout = findViewById<TextInputLayout>(R.id.branchTextInputLayout)
        val rollNumberTextInputLayout = findViewById<TextInputLayout>(R.id.rollNumberTextInputLayout)

        val et_name = findViewById<EditText>(R.id.et_name)
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_batch = findViewById<EditText>(R.id.et_batch)
        val et_branch = findViewById<EditText>(R.id.et_branch)
        val et_rollnumber = findViewById<EditText>(R.id.et_rollnumber)

        // Set hint color based on text presence for each EditText
        setHintColorBasedOnText(et_name, nameTextInputLayout)
        setHintColorBasedOnText(et_email, emailTextInputLayout)
        setHintColorBasedOnText(et_batch, batchTextInputLayout)
        setHintColorBasedOnText(et_branch, branchTextInputLayout)
        setHintColorBasedOnText(et_rollnumber, rollNumberTextInputLayout)

        et_name.addTextChangedListener(createTextWatcher(et_name, nameTextInputLayout))
        et_email.addTextChangedListener(createTextWatcher(et_email, emailTextInputLayout))
        et_batch.addTextChangedListener(createTextWatcher(et_batch, batchTextInputLayout))
        et_branch.addTextChangedListener(createTextWatcher(et_branch, branchTextInputLayout))
        et_rollnumber.addTextChangedListener(createTextWatcher(et_rollnumber, rollNumberTextInputLayout))
    }

    private fun createTextWatcher(editText: EditText, textInputLayout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                setHintColorBasedOnText(editText, textInputLayout)
            }
        }
    }

    fun setHintColorBasedOnText(editText: EditText, textInputLayout: TextInputLayout) {
        if (editText.text.toString().isNotEmpty()) {
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primarycolor))
        } else {
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray))
        }
    }
}
