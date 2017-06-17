package com.anhhoang.popularmovies.model

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

/**
 * Created by AnhHo on 3/20/2017.
 */

class Review(
        @SerializedName("id")
        val id: String,
        @SerializedName("author")
        val author: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("url")
        val url: String
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Review> = object : Parcelable.Creator<Review> {
            override fun createFromParcel(source: Parcel): Review = Review(source)
            override fun newArray(size: Int): Array<Review?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(author)
        dest.writeString(content)
        dest.writeString(url)
    }
}
