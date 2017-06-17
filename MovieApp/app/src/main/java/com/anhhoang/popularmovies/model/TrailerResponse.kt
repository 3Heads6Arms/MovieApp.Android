package com.anhhoang.popularmovies.model

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import com.google.gson.annotations.SerializedName

/**
 * Created by AnhHo on 3/20/2017.
 */

class TrailerResponse(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("results")
        val results: List<Trailer>? = null
)
