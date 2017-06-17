/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 2/4/2017.
 */
package com.anhhoang.popularmovies.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class Genre(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var name: String
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Genre> = object : Parcelable.Creator<Genre> {
            override fun createFromParcel(source: Parcel): Genre = Genre(source)
            override fun newArray(size: Int): Array<Genre?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
    }
}
