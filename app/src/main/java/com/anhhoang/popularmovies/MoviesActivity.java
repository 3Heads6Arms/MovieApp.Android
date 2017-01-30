/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.data.RequestResult;
import com.anhhoang.popularmovies.utils.MoviesApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mMoviesRv;
    private MoviesApiService mMoviesApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mMoviesRv = (RecyclerView) findViewById(R.id.rv_movies);
        mLayoutManager = new GridLayoutManager(this, 2);
        mMoviesAdapter = new MoviesAdapter(this);

        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);

        mMoviesApiService = MoviesApiService.getService();
        mMoviesApiService.discoverMovies(new Callback<RequestResult<Movie>>() {
            @Override
            public void onResponse(Call<RequestResult<Movie>> call, Response<RequestResult<Movie>> response) {
                List<Movie> movieData = response.body().getResults();

                mMoviesAdapter.setMovieData(movieData);
            }

            @Override
            public void onFailure(Call<RequestResult<Movie>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
