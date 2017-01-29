/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MoviesActivity extends AppCompatActivity {
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mMoviesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mMoviesRv = (RecyclerView) findViewById(R.id.rv_movies);
        mLayoutManager = new GridLayoutManager(this, 2);
        mMoviesAdapter = new MoviesAdapter();

        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);

        String[] movies = {
                "Resident Evil",
                "Superman",
                "The Conjuring"
        };

        mMoviesAdapter.setMovieData(movies);
    }
}
