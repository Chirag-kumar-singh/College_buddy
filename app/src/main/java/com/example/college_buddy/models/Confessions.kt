package com.example.college_buddy.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class Confessions(
    val messager : String = "",
    val message : String = "",
    val timestamp: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(messager)
        parcel.writeString(message)
        parcel.writeValue(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Confessions> {
        override fun createFromParcel(parcel: Parcel): Confessions {
            return Confessions(parcel)
        }

        override fun newArray(size: Int): Array<Confessions?> {
            return arrayOfNulls(size)
        }
    }
}
