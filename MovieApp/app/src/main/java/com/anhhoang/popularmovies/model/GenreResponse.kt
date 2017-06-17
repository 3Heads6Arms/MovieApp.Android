/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 2/4/2017.
 */
package com.anhhoang.popularmovies.model

import com.google.gson.annotations.SerializedName

class GenreResponse(
        @SerializedName("genres")
        val genres: List<Genre>
)
