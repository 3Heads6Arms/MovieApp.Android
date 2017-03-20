package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AnhHo on 3/20/2017.
 */

public class TrailerResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
