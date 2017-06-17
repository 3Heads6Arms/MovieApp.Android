/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

import java.util.ArrayList
import java.util.Date

class Movie(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("overview")
        var overview: String? = null,
        @SerializedName("genre_ids")
        var genreIds: List<Int>? = null,
        @SerializedName("poster_path")
        var posterPath: String? = null,
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,
        @SerializedName("adult")
        var isAdult: Boolean = false,
        @SerializedName("release_date")
        var releaseDate: Date? = null,
        @SerializedName("popularity")
        var popularity: Double = 0.toDouble(),
        @SerializedName("vote_count")
        var voteCount: Int = 0,
        @SerializedName("vote_average")
        var voteAverage: Double = 0.toDouble(),
        @SerializedName("video")
        var isHasVideo: Boolean = false,
        var isUserFavorite: Boolean = false
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie = Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            ArrayList<Int>().apply { source.readList(this, Int::class.java.classLoader) },
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readSerializable() as Date?,
            source.readDouble(),
            source.readInt(),
            source.readDouble(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(overview)
        dest.writeList(genreIds)
        dest.writeString(posterPath)
        dest.writeString(backdropPath)
        dest.writeInt((if (isAdult) 1 else 0))
        dest.writeSerializable(releaseDate)
        dest.writeDouble(popularity)
        dest.writeInt(voteCount)
        dest.writeDouble(voteAverage)
        dest.writeInt((if (isHasVideo) 1 else 0))
        dest.writeInt((if (isUserFavorite) 1 else 0))
    }
}
