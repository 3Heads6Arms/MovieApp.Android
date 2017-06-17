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

class Trailer(
        @SerializedName("id")
        var id: String,
        @SerializedName("iso6391")
        var iso6391: String,
        @SerializedName("iso31161")
        var iso31161: String,
        @SerializedName("key")
        var key: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("site")
        var site: String,
        @SerializedName("size")
        var size: Int,
        @SerializedName("type")
        var type: String
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Trailer> = object : Parcelable.Creator<Trailer> {
            override fun createFromParcel(source: Parcel): Trailer = Trailer(source)
            override fun newArray(size: Int): Array<Trailer?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(iso6391)
        dest.writeString(iso31161)
        dest.writeString(key)
        dest.writeString(name)
        dest.writeString(site)
        dest.writeInt(size)
        dest.writeString(type)
    }
}
