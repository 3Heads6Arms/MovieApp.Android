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

public class Movie implements IMovie {
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
    private boolean isUserFavorite;

    public Movie() {
    }

    public Movie(int id) {
        this.id = id;
    }

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
        isUserFavorite = parcel.readByte() != 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    @Override
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public String getBackdropPath() {
        return backdropPath;
    }

    @Override
    public boolean isAdult() {
        return isAdult;
    }

    @Override
    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public double getPopularity() {
        return popularity;
    }

    @Override
    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public boolean isHasVideo() {
        return hasVideo;
    }

    @Override
    public boolean isUserFavorite() {
        return isUserFavorite;
    }

    @Override
    public void setUserFavorite(boolean userFavorite) {
        isUserFavorite = userFavorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Movie) {
            return ((Movie) obj).id == this.id;
        }

        return false;
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
        dest.writeByte((byte) (isUserFavorite ? 1 : 0));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
