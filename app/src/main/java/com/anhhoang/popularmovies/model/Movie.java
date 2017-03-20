/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("adult")
    private boolean isAdult;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("video")
    private boolean hasVideo;

    public Movie(Parcel parcel) {
        genreIds = new ArrayList<>();

        id = parcel.readInt();
        title = parcel.readString();
        overview = parcel.readString();
        parcel.readList(genreIds, List.class.getClassLoader());
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        isAdult = parcel.readByte() != 0;
        releaseDate = new Date(parcel.readLong());
        popularity = parcel.readDouble();
        voteCount = parcel.readInt();
        voteAverage = parcel.readDouble();
        hasVideo = parcel.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeList(genreIds);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeLong(releaseDate.getTime());
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}
