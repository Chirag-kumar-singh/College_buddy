package com.example.college_buddy.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.Activities.BaseActivity
import com.example.college_buddy.Activities.PdfViewActivity
import com.example.college_buddy.R
import com.example.college_buddy.models.Exam_paper

open class ExamPaperItemsAdapter(private val context: Context,
    private val list: ArrayList<Exam_paper>):

RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_exam_paper, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            holder.textViewSubject.text = model.subject
            holder.textViewBranch.text = model.branch
            holder.textViewType.text = model.type
            holder.textViewYear.text = model.year
            holder.textViewName.text = model.name

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }

            holder.recImage.setOnClickListener {
                // Get the URI of the PDF file
                val pdfUri = Uri.parse(model.pdf)
                // Create Intent to start PdfViewActivity and pass URI as extra
                val intent = Intent(context, PdfViewActivity::class.java).apply {
                    putExtra("PDF_URI", pdfUri.toString())
                }
                // Start the PdfViewActivity
                context.startActivity(intent)
            }

        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Exam_paper)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewSubject: TextView = view.findViewById(R.id.textViewSubject)
        val textViewBranch: TextView = view.findViewById(R.id.textViewbranch)
        val textViewType: TextView = view.findViewById(R.id.textViewType)
        val textViewYear: TextView = view.findViewById(R.id.textViewYear)
        val textViewName: TextView = view.findViewById(R.id.recName)
        val textViewLang: TextView = view.findViewById(R.id.recLang)

        val recImage: ImageView = view.findViewById(R.id.recImage)
    }



}