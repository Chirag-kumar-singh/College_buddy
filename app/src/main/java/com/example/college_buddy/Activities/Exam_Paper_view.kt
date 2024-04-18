package com.example.college_buddy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.adapters.ExamPaperItemsAdapter
import com.example.college_buddy.models.Exam_paper

class Exam_Paper_view : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_paper_view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val selectedBranch = intent.getStringExtra("BRANCH")

        // Call FirestoreClass to fetch exam papers for the selected branch
        FirestoreClass().getExamPapersList(this, selectedBranch ?: "")
    }

    fun populateExamPapersListToUI(exampaperslist : ArrayList<Exam_paper>){
        //hideProgressDialog()
        var rv_exampapers_list = findViewById<RecyclerView>(R.id.rv_exampapers_list)
        var tv_no_papers_available = findViewById<TextView>(R.id.tv_no_papers_available)

        if(exampaperslist.size > 0){
             rv_exampapers_list.visibility = View.VISIBLE
            tv_no_papers_available.visibility = View.GONE


            rv_exampapers_list.layoutManager = LinearLayoutManager(this)
            rv_exampapers_list.setHasFixedSize(true)

            val adapter = ExamPaperItemsAdapter(this, exampaperslist)
            rv_exampapers_list.adapter = adapter
        }else{
            rv_exampapers_list.visibility = View.GONE
            tv_no_papers_available.visibility = View.VISIBLE
        }
    }
}