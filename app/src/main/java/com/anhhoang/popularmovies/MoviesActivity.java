/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.data.MovieResponse;
import com.anhhoang.popularmovies.utils.MoviesApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {
    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRv;
    private ProgressBar mLoadingIndicatorPb;

    private MoviesApiService mMoviesApiService;

    private MoviesAdapter.OnMovieItemClickListener mOnClickListener = new MoviesAdapter.OnMovieItemClickListener() {
        @Override
        public void onClick(Movie movie) {
            Intent movieDetailIntent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_DATA, movie);
            startActivity(movieDetailIntent);
        }
    };
    private Callback<MovieResponse<Movie>> mMoviesRequestCallback = new Callback<MovieResponse<Movie>>() {
        @Override
        public void onResponse(Call<MovieResponse<Movie>> call, Response<MovieResponse<Movie>> response) {
            mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
            if (response.body() != null) {
                List<Movie> movieData = response.body().getResults();

                mMoviesAdapter.setMovieData(movieData);
            }
        }

        @Override
        public void onFailure(Call<MovieResponse<Movie>> call, Throwable t) {
            mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
            t.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        mLoadingIndicatorPb = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mLayoutManager = new GridLayoutManager(this, 2);
        mMoviesAdapter = new MoviesAdapter(this, mOnClickListener);

        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);
        mMoviesApiService = MoviesApiService.getService();

        mLoadingIndicatorPb.setVisibility(View.VISIBLE);
        mMoviesApiService.getMoviesByPopularity(mMoviesRequestCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // Clear up adapter's data and shows progress bar
        if (itemId == R.id.action_sort_popularity) {
            mLoadingIndicatorPb.setVisibility(View.VISIBLE);
            mMoviesAdapter.setMovieData(null);
            mMoviesApiService.getMoviesByPopularity(mMoviesRequestCallback);
            return true;
        } else if (itemId == R.id.action_sort_top_rated) {
            mLoadingIndicatorPb.setVisibility(View.VISIBLE);
            mMoviesAdapter.setMovieData(null);
            mMoviesApiService.getMoviesByTopRating(mMoviesRequestCallback);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
