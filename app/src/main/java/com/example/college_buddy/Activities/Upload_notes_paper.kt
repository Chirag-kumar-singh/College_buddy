package com.example.college_buddy.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.college_buddy.Firebase.FirestoreClass
import com.example.college_buddy.R
import com.example.college_buddy.models.Exam_paper
import com.example.college_buddy.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Upload_notes_paper : BaseActivity() {


    private var mSelectedPdfFileUri: Uri? = null

    private lateinit var mUserName: String

    private var mExamPaperPdfURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_notes_paper)

        setSpinners()

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        var selectpdftextview = findViewById<TextView>(R.id.selectpdftextview)
        selectpdftextview.setOnClickListener { view ->
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showPdfChooser(this@Upload_notes_paper)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        val uploadexampdfButton = findViewById<Button>(R.id.uploadexampdfButton)
        uploadexampdfButton.setOnClickListener {
            if(mSelectedPdfFileUri != null){
                uploadExamPaperPdf()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                uploadExamPaper()
            }
        }
    }

    private fun uploadExamPaper(){
        val UploadedBy = getCurrentUserId()
        var pdfname = findViewById<TextView>(R.id.pdfname)
        var branchofpaperSpinner = findViewById<Spinner>(R.id.branchofpaperSpinner)
        var yearofpaper = findViewById<TextView>(R.id.yearofpaper)
        var subjectofpaper = findViewById<TextView>(R.id.subjectofpaper)
        //var typeofpaperSpinner = findViewById<Spinner>(R.id.typeofpaperSpinner)

        val selectedBranch = branchofpaperSpinner.selectedItem.toString()
        //val selectedType = typeofpaperSpinner.selectedItem.toString()
        var Exam_paper = Exam_paper(
            pdfname.text.toString(),
            selectedBranch,
            yearofpaper.text.toString(),
            subjectofpaper.text.toString(),
            mExamPaperPdfURL,
            UploadedBy
        )

        FirestoreClass().uploadnotespaper(this@Upload_notes_paper, Exam_paper)
    }

    private fun uploadExamPaperPdf(){
        showProgressDialog(resources.getString(R.string.please_wait))

        //getting the storage reference

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "NOTES_PAPER_PDF" + System.currentTimeMillis()
                    + "." + Constants.getFileExtension(this, mSelectedPdfFileUri)
        )

        //adding the file to reference
        sRef.putFile(mSelectedPdfFileUri!!).addOnSuccessListener { taskSnapshot ->
            //image upload is success
            Log.i(
                "Firebase NOTES_PAPER_PDF URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            //Get the downloadable url from the task snapshot.
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.i("Downloadable PDF URI", uri.toString())
                //assign the image url to variable.
                mExamPaperPdfURL = uri.toString()

                uploadExamPaper()
            }
        }.addOnFailureListener{
                exception ->
            Toast.makeText(
                this@Upload_notes_paper,
                exception.message,
                Toast.LENGTH_SHORT
            ).show()

            hideProgressDialog()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@Upload_notes_paper)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_PICK_PDF && resultCode == Activity.RESULT_OK && data!!.data!= null) {
            mSelectedPdfFileUri = data.data
            displayPdfPreview(mSelectedPdfFileUri!!)
        }
    }

    private fun displayPdfPreview(pdfUri: Uri) {
        val imageView = findViewById<ImageView>(R.id.uploadPdfImage)

        try {
            val fileDescriptor = contentResolver.openFileDescriptor(pdfUri, "r")
            fileDescriptor?.use { fd ->
                val pdfRenderer = PdfRenderer(fd)
                val renderer = pdfRenderer.openPage(0)
                val bitmap = Bitmap.createBitmap(renderer.width, renderer.height, Bitmap.Config.ARGB_8888)
                renderer.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                imageView.setImageBitmap(bitmap)

                renderer.close()
                pdfRenderer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception
        }
    }

    fun ExamPaperUploadedSuccessfully(){
        hideProgressDialog()
        finish()
    }

    fun setSpinners(){
        val branches = arrayOf("Select Branch of notes", "CSE", "AIDS", "ECE", "MECH")
        val spinner: Spinner = findViewById(R.id.branchofpaperSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branches)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

//        val branches2 = arrayOf("Select type of exam", "Minor1", "Minor2", "EndSem", "Lab")
//        val spinner2: Spinner = findViewById(R.id.typeofpaperSpinner)
//        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, branches2)
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner2.adapter = adapter2

    }
}