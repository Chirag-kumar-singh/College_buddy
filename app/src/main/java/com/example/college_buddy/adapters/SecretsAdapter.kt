package com.example.college_buddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.R
import com.example.college_buddy.models.Confessions

class SecretsAdapter(private  val secretsList : List<Confessions>) : RecyclerView.Adapter<SecretsAdapter.SecretsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecretsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.secrets_item,parent,false)
        return SecretsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SecretsViewHolder, position: Int) {
        val currentItem = secretsList[position]
        holder.secretTextView.text = currentItem.message
    }

    override fun getItemCount()=secretsList.size

    class  SecretsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val secretTextView : TextView = itemView.findViewById(R.id.secretText)
    }
}