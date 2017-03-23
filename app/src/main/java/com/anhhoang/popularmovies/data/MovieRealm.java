package com.anhhoang.popularmovies.data;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.anhhoang.popularmovies.model.IMovie;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AnhHo on 3/22/2017.
 */

public class MovieRealm extends RealmObject implements IMovie {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    private RealmList<IntegerRealm> genreIds;
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

    public MovieRealm() {
    }

    public MovieRealm(Parcel parcel) {
        List<Integer> genres = new ArrayList<>();

        id = parcel.readInt();
        title = parcel.readString();
        overview = parcel.readString();
        parcel.readList(genres, List.class.getClassLoader());
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        isAdult = parcel.readByte() != 0;
        releaseDate = new Date(parcel.readLong());
        popularity = parcel.readDouble();
        voteCount = parcel.readInt();
        voteAverage = parcel.readDouble();
        hasVideo = parcel.readByte() != 0;
        isUserFavorite = parcel.readByte() != 0;

        genreIds = IntegerRealm.parse(genres);
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
        return IntegerRealm.parseTo(genreIds);
    }

    public void setGenreIds(RealmList<IntegerRealm> genreIds) {
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
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<Integer> genres = IntegerRealm.parseTo(genreIds);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeList(genres);
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

    public static final Parcelable.Creator<MovieRealm> CREATOR = new Parcelable.Creator<MovieRealm>() {

        @Override
        public MovieRealm createFromParcel(Parcel source) {
            return new MovieRealm(source);
        }

        @Override
        public MovieRealm[] newArray(int size) {
            return new MovieRealm[size];
        }
    };
}
