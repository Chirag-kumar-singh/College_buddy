package com.example.college_buddy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.college_buddy.R
import com.example.recyclerviewkotlin.LinksItemsAdapter
import com.example.recyclerviewkotlin.Links_data_class

class Link_view : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Links_data_class>
    lateinit var imageList:Array<Int>
    lateinit var titleList:Array<String>
    lateinit var linklist:Array<String>
    private lateinit var myAdapter: LinksItemsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_view)

        imageList = arrayOf(
            R.drawable.iiitdm_logo,
            R.drawable.moodle_logo,
            R.drawable.codetantra_logo,
            R.drawable.iiitdm_logo,
            R.drawable.linkedin_logo,
        )

        titleList = arrayOf(
            "IIITDM Kurnool",
            "Moodle",
            "Codetantra",
            "AIMS",
            "Linkedin")

        linklist = arrayOf(
            "https://iiitk.ac.in/",
            "http://iiitk.eabyas.in/login/index.php",
            "https://iiitdmkl.codetantra.com/login.jsp",
            "https://aims.iiitk.ac.in/aimskurnool/",
            "https://www.linkedin.com/company/placement-cell-iiitdm-kurnool/"
        )


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<Links_data_class>()
        getData()

    }

    private fun getData(){
        for (i in imageList.indices){
            val dataClass = Links_data_class(imageList[i], titleList[i], linklist[i])
            dataList.add(dataClass)
        }
        //searchList.addAll(dataList)
        recyclerView.adapter = LinksItemsAdapter(this, dataList)
    }
}