package com.example.college_buddy.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.User
import com.example.college_buddy.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, FirestoreClass.UserDataListener{

    companion object{
        //A unique code for starting the activity for result.
        const val MY_PROFILE_REQUEST_CODE : Int = 11
    }

    private lateinit var mUserName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUserName = "Default User"

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val firestoreClass = FirestoreClass()
        firestoreClass.setUserDataListener(this)

        // Load user data
        firestoreClass.loadUserData(this)

        var navigationbaroperner = findViewById<ImageView>(R.id.navigationbaroperner)
        navigationbaroperner.setOnClickListener {
            toggleDrawer()
        }

        //setupActionBar()

        var nav_view = findViewById<NavigationView>(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

        var Logout = findViewById<TextView>(R.id.Logout)

        Logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        var ExampaperCard = findViewById<CardView>(R.id.ExamPaperCard)
        ExampaperCard.setOnClickListener {
            val intent = Intent(this, Upload_exam_paper::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            val intent2 = Intent(this, ExamPapersActivity::class.java)
            startActivity(intent2)
        }

        var NotesCard = findViewById<CardView>(R.id.NotesCard)
        NotesCard.setOnClickListener {
            val intent = Intent(this, Upload_notes_paper::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            val intent2 = Intent(this, NotesPapersActivity::class.java)
            startActivity(intent2)
        }

        var LinksCard = findViewById<CardView>(R.id.LinksCard)
        LinksCard.setOnClickListener {
            val intent = Intent(this, Link_view::class.java)
            startActivity(intent)
        }

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
//        navController = navHostFragment.navController

        // Set up bottom navigation
        var bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.homeImage
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.secretsFeed -> {
                    val intent = Intent(this, Secrets_feeds::class.java)
                    intent.putExtra(Constants.NAME, mUserName)
                    startActivity(intent)
                    true
                }
                // Add other cases for additional menu items here
                else -> false
            }
        }
    }

//    private fun navigateToFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment_activity_main, fragment)
//            .commit()
//    }

    override fun onUserDataLoaded(user: User) {
        // Update mUserName property
        mUserName = user.name
        // Update UI or perform other tasks
        updateMainScreenUserDetails(user)
    }



//    private fun setupActionBar(){
//        var toolbar_main_activity = findViewById<Toolbar>(R.id.toolbar_main_activity)
//        setSupportActionBar(toolbar_main_activity)
//        supportActionBar?.title = ""
//        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
//        toolbar_main_activity.setNavigationOnClickListener {
//            //Toggle drawer
//            toggleDrawer()
//        }
//    }

    private fun toggleDrawer(){
        var drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        var drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBacktoExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this@MainActivity)
        }else{
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                startActivityForResult(Intent(this,
                    MyProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }
        }
        var drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User){

        mUserName = user.name

        var nav_user_iamge = findViewById<CircleImageView>(R.id.nav_user_image)
        var tv_username = findViewById<TextView>(R.id.tv_username)
        var tv_email = findViewById<TextView>(R.id.tv_email)

        //Load the user image in the ImageView.
        Glide
            .with(this@MainActivity)
            .load(user.image)   //URL of the image
            .centerCrop()       //Scale type of the image.
            .placeholder(R.mipmap.profile_user_foreground)   //A default place holder.
            .into(nav_user_iamge)   //the view in which the image will be loaded.

        tv_username.text = user.name
        tv_email.text = user.email

//        if(readBoardList){
//            showProgressDialog(resources.getString(R.string.please_wait))
//            FireStoreClass().getBoardList(this)
//
//        }
    }

    fun updateMainScreenUserDetails(user: User){
        var main_profile_user_image = findViewById<CircleImageView>(R.id.main_profile_user_image)
        var main_user_name = findViewById<TextView>(R.id.main_user_name)
        var main_branch = findViewById<TextView>(R.id.main_branch)
        var main_roll_no = findViewById<TextView>(R.id.main_roll_no)

        //Load the user image in the ImageView.
        Glide
            .with(this@MainActivity)
            .load(user.image)   //URL of the image
            .centerCrop()       //Scale type of the image.
            .placeholder(R.mipmap.profile_user_foreground)   //A default place holder.
            .into(main_profile_user_image)   //the view in which the image will be loaded.

        main_user_name.text = user.name
        main_branch.text = user.branch
        main_roll_no.text = user.rollno

    }
}