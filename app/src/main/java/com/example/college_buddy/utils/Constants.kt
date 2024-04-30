package com.example.college_buddy.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap


object Constants{
    const val USERS: String = "users"

    const val NAME: String = "name"
    const val IMAGE: String = "image"

    const val EXAM_PAPER: String = "exam_paper"

    const val NOTES_PAPER: String = "notes_paper"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult
    const val READ_STORAGE_PERMISSION_CODE = 1
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    const val REQUEST_CODE_PICK_PDF = 3



    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity){
        //An intent for launching the image selection of phone storage.
        var galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // Launches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the extension of selected image.
     */
    fun getFileExtension(activity: Activity, uri: Uri?):String?{

        /*
        MimeTypeMap: Two way map that maps MIME-type to its file extensions and vice versa.

        getSingleton(): Get the singleton instance of Mimetype

        getExtensionFromMimeType: Return the registered extension for given MIME type.

        contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun showPdfChooser(activity: Activity){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PDF)
    }
}