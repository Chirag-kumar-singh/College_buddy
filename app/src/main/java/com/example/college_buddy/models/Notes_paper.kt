package com.example.college_buddy.models

import android.os.Parcel
import android.os.Parcelable

data class Notes_paper( val name: String = "",
                        val branch: String = "",
                        val year: String = "",
                        val subject: String = "",
                        val pdf: String = "",
                        val uploadedBy: String = "",
                        val likes: Int = 0,
                        val dislikes: Int = 0,
                        var documentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(branch)
        parcel.writeString(year)
        parcel.writeString(subject)
        parcel.writeString(pdf)
        parcel.writeString(uploadedBy)
        parcel.writeInt(likes)
        parcel.writeInt(dislikes)
        parcel.writeString(documentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notes_paper> {
        override fun createFromParcel(parcel: Parcel): Notes_paper {
            return Notes_paper(parcel)
        }

        override fun newArray(size: Int): Array<Notes_paper?> {
            return arrayOfNulls(size)
        }
    }
}