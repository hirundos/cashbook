package com.example.dkdus.cashbook.model

import android.os.Parcel
import android.os.Parcelable

data class ChildList(var type: String?, var contents: String?, var money: String?,
                     var date: String?, var category: String?, var time: String?
                    ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(contents)
        parcel.writeString(money)
        parcel.writeString(date)
        parcel.writeString(category)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChildList> {
        override fun createFromParcel(parcel: Parcel): ChildList {
            return ChildList(parcel)
        }

        override fun newArray(size: Int): Array<ChildList?> {
            return arrayOfNulls(size)
        }
    }
}
