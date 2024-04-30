package com.example.college_buddy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.adapters.ExamPaperItemsAdapter
import com.example.college_buddy.adapters.NotesPaperItemsAdapter
import com.example.college_buddy.models.Exam_paper
import com.example.college_buddy.models.Notes_paper

class Notes_Paper_view : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_paper_view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val selectedBranch = intent.getStringExtra("BRANCH")

        // Call FirestoreClass to fetch exam papers for the selected branch
        FirestoreClass().getNotesPapersList(this, selectedBranch ?: "")
    }

    fun populateNotesPapersListToUI(notespaperslist : ArrayList<Notes_paper>){
        //hideProgressDialog()
        var rv_notespapers_list = findViewById<RecyclerView>(R.id.rv_notespapers_list)
        var tv_no_notes_available = findViewById<TextView>(R.id.tv_no_notes_available)

        if(notespaperslist.size > 0){
            rv_notespapers_list.visibility = View.VISIBLE
            tv_no_notes_available.visibility = View.GONE


            rv_notespapers_list.layoutManager = LinearLayoutManager(this)
            rv_notespapers_list.setHasFixedSize(true)

            val adapter = NotesPaperItemsAdapter(this, notespaperslist)
            rv_notespapers_list.adapter = adapter
        }else{
            rv_notespapers_list.visibility = View.GONE
            tv_no_notes_available.visibility = View.VISIBLE
        }
    }
}