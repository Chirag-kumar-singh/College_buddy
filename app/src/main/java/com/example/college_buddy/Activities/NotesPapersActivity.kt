package com.example.college_buddy.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.User

class NotesPapersActivity : BaseActivity() {

    private lateinit var mUserName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_papers)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        FirestoreClass().loadUserData(this)

        var Addpdf = findViewById<TextView>(R.id.AddPdf)
        Addpdf.setOnClickListener{

            val intent = Intent(this, Upload_notes_paper::class.java)
            startActivity(intent)
        }

        var CSECard = findViewById<CardView>(R.id.CSECard)
        val AIDSCard = findViewById<CardView>(R.id.AIDSCard)
        val ECECard = findViewById<CardView>(R.id.ECECard)
        val MECHCard = findViewById<CardView>(R.id.MECHCard)
        CSECard.setOnClickListener {

            val intent = Intent(this, Notes_Paper_view::class.java)
            intent.putExtra("BRANCH", "CSE")
            startActivity(intent)
            //showProgressDialog(resources.getString(R.string.please_wait))
            //FirestoreClass().getExamPapersList(Exam_Paper_view())
        }

        AIDSCard.setOnClickListener {

            val intent = Intent(this, Notes_Paper_view::class.java)
            intent.putExtra("BRANCH", "AIDS")
            startActivity(intent)
            //showProgressDialog(resources.getString(R.string.please_wait))
            //FirestoreClass().getExamPapersList(Exam_Paper_view())
        }

        ECECard.setOnClickListener {

            val intent = Intent(this, Notes_Paper_view::class.java)
            intent.putExtra("BRANCH", "ECE")
            startActivity(intent)
            //showProgressDialog(resources.getString(R.string.please_wait))
            //FirestoreClass().getExamPapersList(Exam_Paper_view())
        }

        MECHCard.setOnClickListener {

            val intent = Intent(this, Notes_Paper_view::class.java)
            intent.putExtra("BRANCH", "MECH")
            startActivity(intent)
            //showProgressDialog(resources.getString(R.string.please_wait))
            //FirestoreClass().getExamPapersList(Exam_Paper_view())
        }



    }

    fun getUserDetails(user: User){
        mUserName = user.name
    }

}