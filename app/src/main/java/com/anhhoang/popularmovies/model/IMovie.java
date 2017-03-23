package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by AnhHo on 3/22/2017.
 */

// Created to ensure similar interface since there are two types of Movie objects
public interface IMovie extends Parcelable {
    int getId();

    String getTitle();

    String getOverview();

    List<Integer> getGenreIds();

    String getPosterPath();

    String getBackdropPath();

    boolean isAdult();

    Date getReleaseDate();

    double getPopularity();

    int getVoteCount();

    double getVoteAverage();

    boolean isHasVideo();

    boolean isUserFavorite();

    void setUserFavorite(boolean userFavorite);
}
