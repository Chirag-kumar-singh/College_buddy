package com.example.college_buddy.models

import android.os.Parcel
import android.os.Parcelable

data class User (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val batch: Long = 0,
    val branch: String = "",
    val rollno : String = "",
    val fcmToken: String = "",
    val isadmin: String = "0",
    val image:String = ""

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeLong(batch)
        parcel.writeString(branch)
        parcel.writeString(rollno)
        parcel.writeString(fcmToken)
        parcel.writeString(isadmin)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}