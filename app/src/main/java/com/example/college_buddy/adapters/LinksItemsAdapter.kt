package com.example.recyclerviewkotlin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.R

class LinksItemsAdapter(private val context : Context, private val dataList: ArrayList<Links_data_class>): RecyclerView.Adapter<LinksItemsAdapter.ViewHolderClass>() {

    var onItemClick: ((Links_data_class) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_links, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.dataImage)
        holder.rvTitle.text = currentItem.dataTitle

        holder.itemView.setOnClickListener{
            var link = currentItem.dataLinks
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            onItemClick?.invoke(currentItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvImage:ImageView = itemView.findViewById(R.id.image)
        val rvTitle:TextView = itemView.findViewById(R.id.title)
    }
}