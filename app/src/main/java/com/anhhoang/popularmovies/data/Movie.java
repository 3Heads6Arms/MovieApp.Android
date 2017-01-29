/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.data;

import java.util.Date;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String overview;
    private List<Integer> genreIds;
    private String posterPath;
    private String backdropPath;
    private boolean isAdult;
    private Date releaseDate;
    private double popularity;
    private int voteCount;
    private double voteAverage;
    private boolean hasVideo;

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
}
