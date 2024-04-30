package com.example.college_buddy.Firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.college_buddy.Activities.ExamPapersActivity
import com.example.college_buddy.Activities.Exam_Paper_view
import com.example.college_buddy.Activities.LoginActivity
import com.example.college_buddy.Activities.MainActivity
import com.example.college_buddy.Activities.MyProfileActivity
import com.example.college_buddy.Activities.Notes_Paper_view
import com.example.college_buddy.Activities.SignupActivity
import com.example.college_buddy.Activities.Upload_exam_paper
import com.example.college_buddy.Activities.Upload_notes_paper
import com.example.college_buddy.models.Exam_paper
import com.example.college_buddy.models.Notes_paper
import com.example.college_buddy.models.User
import com.example.college_buddy.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {

    interface UserDataListener {
        fun onUserDataLoaded(user: User)
    }

    private val mFireStore = FirebaseFirestore.getInstance()

    private var mUserDataListener: UserDataListener? = null

    fun setUserDataListener(listener: UserDataListener) {
        mUserDataListener = listener
    }

    fun registerUser(activity: SignupActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener{
                e->
                Log.e(activity.javaClass.simpleName, "Errorr")
            }
    }

    /**
     * A function to update the user profile data into the database.
     */
    fun updateUserProfileData(activity: Activity,
                              userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                when(activity){
                    is MyProfileActivity -> {
                        //Notify the success result.
                        activity.profileUpdateSuccess()
                    }
                }


            }.addOnFailureListener {
                    e->
                when(activity){
                    is MyProfileActivity -> {
                        //Notify the success result.
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board",
                    e
                )
                Toast.makeText(activity, "Error while updating the profile!", Toast.LENGTH_SHORT).show()

            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                val loggedInUser = document.toObject(User::class.java)!!
                mUserDataListener?.onUserDataLoaded(loggedInUser)

                when (activity){
                    is LoginActivity -> {
                        if(loggedInUser != null)
                            activity.logInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                        activity.updateMainScreenUserDetails(loggedInUser)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }

                    is ExamPapersActivity -> {
                        activity.getUserDetails(loggedInUser)
                    }
                }
            }.addOnFailureListener{

                    e->
                when (activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Errorr")
            }
    }

    fun uploadexampaper(activity: Upload_exam_paper, examPaper: Exam_paper){
        mFireStore.collection(Constants.EXAM_PAPER)
            .document()
            .set(examPaper, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Exam Paper uploaded successfully.")

                Toast.makeText(activity,
                    "Exam Paper uploaded successfully", Toast.LENGTH_SHORT).show()
                activity.ExamPaperUploadedSuccessfully()
            }
            .addOnFailureListener {
                exception ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading pdf",
                    exception
                )
            }
    }

    fun uploadnotespaper(activity: Upload_notes_paper, examPaper: Exam_paper){
        mFireStore.collection(Constants.NOTES_PAPER)
            .document()
            .set(examPaper, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Notes uploaded successfully.")

                Toast.makeText(activity,
                    "Notes uploaded successfully", Toast.LENGTH_SHORT).show()
                activity.ExamPaperUploadedSuccessfully()
            }
            .addOnFailureListener {
                    exception ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading pdf",
                    exception
                )
            }
    }

    fun getExamPapersList(activity: Exam_Paper_view, branch: String){
        mFireStore.collection(Constants.EXAM_PAPER)
            .whereEqualTo("branch", branch)
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val ExamPapersList: ArrayList<Exam_paper> = ArrayList()
                for (i in document.documents){
                    val exam_paper = i.toObject(Exam_paper::class.java)!!
                    exam_paper.documentId = i.id
                    ExamPapersList.add(exam_paper)
                }

                activity.populateExamPapersListToUI(ExamPapersList)
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while displaying papers.", e)
            }
    }

    fun getNotesPapersList(activity: Notes_Paper_view, branch: String){
        mFireStore.collection(Constants.NOTES_PAPER)
            .whereEqualTo("branch", branch)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val NotesPapersList: ArrayList<Notes_paper> = ArrayList()
                for (i in document.documents){
                    val notes_paper = i.toObject(Notes_paper::class.java)!!
                    notes_paper.documentId = i.id
                    NotesPapersList.add(notes_paper)
                }

                activity.populateNotesPapersListToUI(NotesPapersList)
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while displaying papers.", e)
            }
    }

    fun getCurrentUserID(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null)
            currentUserId = currentUser.uid

        return currentUserId
    }

}