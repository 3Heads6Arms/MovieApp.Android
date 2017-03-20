package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AnhHo on 3/20/2017.
 */

public class ReviewResponse {
    @SerializedName("id")
    private Integer id;
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<Review> results;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;
}
