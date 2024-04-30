package com.example.recyclerviewkotlin

import android.os.Parcel
import android.os.Parcelable

data class Links_data_class(var dataImage:Int, var dataTitle:String, var dataLinks:String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dataImage)
        parcel.writeString(dataTitle)
        parcel.writeString(dataLinks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Links_data_class> {
        override fun createFromParcel(parcel: Parcel): Links_data_class {
            return Links_data_class(parcel)
        }

        override fun newArray(size: Int): Array<Links_data_class?> {
            return arrayOfNulls(size)
        }
    }
}