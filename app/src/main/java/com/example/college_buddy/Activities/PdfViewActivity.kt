package com.example.college_buddy.Activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.example.college_buddy.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PdfViewActivity : BaseActivity() {

    private lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        pdfView = findViewById(R.id.pdfView)
        val pdfUriString = intent.getStringExtra("PDF_URI")

        // Parse the URI string into a Uri object
        val pdfUri = Uri.parse(pdfUriString)

        // Load the PDF file into the PDFView
        //pdfView.fromUri(pdfUri).load()

        showProgressDialog(resources.getString(R.string.please_wait))
        lifecycleScope.launch(Dispatchers.IO){
            val inputStream = URL(pdfUriString).openStream()
            withContext(Dispatchers.Main){
                pdfView.fromStream(inputStream).onRender{
                    pages, pageWidth, pageHeight ->

                    if(pages >= 1){
                        hideProgressDialog()
                    }

                }.load()
            }
        }
    }
}