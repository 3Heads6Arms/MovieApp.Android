/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/30/2017.
 */
package com.anhhoang.popularmovies.model

import com.google.gson.annotations.SerializedName

class MovieResponse(
        @SerializedName("page")
        val page: Int? = null,
        @SerializedName("results")
        val results: List<Movie>? = null,
        @SerializedName("total_results")
        val totalResults: Int? = null,
        @SerializedName("total_pages")
        val totalPages: Int? = null
)
