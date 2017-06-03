/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.anhhoang.popularmovies.data.FavoriteMovieContract;
import com.anhhoang.popularmovies.model.Movie;
import com.anhhoang.popularmovies.model.MovieResponse;
import com.anhhoang.popularmovies.utils.FavoriteMovieUtils;
import com.anhhoang.popularmovies.utils.MoviesApiService;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {
    public static final int FAVORITE_ADDED = 9;
    public static final int FAVORITE_REMOVED = 10;
    public static final int NO_ACTION_TAKEN = 0;

    private static final int REQUEST_CODE = 33;
    private static final String SELECTED_MENU_KEY = "SelectedSetting";
    private static final String RECYCLER_LAYOUT_KEY = "RecyclerPosition";
    private static final String ADAPTER_DATA_KEY = "AdapterData";

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRv;
    private ProgressBar mLoadingIndicatorPb;

    private MoviesApiService mMoviesApiService;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;
    private int mSelectedSetting;

    private MoviesAdapter.OnMovieItemClickListener mOnClickListener = new MoviesAdapter.OnMovieItemClickListener() {
        @Override
        public void onClick(Movie movie) {
            Intent movieDetailIntent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_DATA, movie);
            startActivityForResult(movieDetailIntent, REQUEST_CODE);
        }
    };
    private Callback<MovieResponse> mMoviesRequestCallback = new Callback<MovieResponse>() {
        @Override
        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
            if (response.body() != null) {
                List<Movie> movieData = response.body().getResults();

                mMoviesAdapter.setMovieData(movieData);
            } else if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // TODO:
            }
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
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

        int gridRowItems = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        mSelectedSetting = R.id.action_sort_popularity;

        mLoadingIndicatorPb = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mLayoutManager = new GridLayoutManager(this, gridRowItems);
        mMoviesAdapter = new MoviesAdapter(this, mOnClickListener);

        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);
        mMoviesApiService = MoviesApiService.getService();

        if (savedInstanceState == null) {
            mLoadingIndicatorPb.setVisibility(View.VISIBLE);
            mMoviesApiService.getMoviesByPopularity(mMoviesRequestCallback);
        } else {
            mSelectedSetting = savedInstanceState.getInt(SELECTED_MENU_KEY);
            List<Movie> movies = savedInstanceState.getParcelableArrayList(ADAPTER_DATA_KEY);
            Parcelable layoutParcelable = savedInstanceState.getParcelable(RECYCLER_LAYOUT_KEY);

            mMoviesAdapter.setMovieData(movies);
            mLayoutManager.onRestoreInstanceState(layoutParcelable);
            mLayoutManager.setSpanCount(gridRowItems);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable layoutParcelable = mLayoutManager.onSaveInstanceState();
        ArrayList<Movie> movies = new ArrayList<>(mMoviesAdapter.getMovieData());

        outState.putParcelableArrayList(ADAPTER_DATA_KEY, movies);
        outState.putParcelable(RECYCLER_LAYOUT_KEY, layoutParcelable);
        outState.putInt(SELECTED_MENU_KEY, mSelectedSetting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        mSelectedSetting = itemId;

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
        } else if (itemId == R.id.action_favorite) {
            loadFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle result only if Favorite Setting selected
        if (mSelectedSetting == R.id.action_favorite &&
                requestCode == REQUEST_CODE &&
                resultCode != NO_ACTION_TAKEN &&
                data != null) {
            int movieId = data.getIntExtra(Intent.EXTRA_INDEX, -1);
            if (movieId != -1) {
                if (resultCode == FAVORITE_ADDED) {
                    // Get movie from persisted DB, there is any should returns only 1 result.
                    Uri queryUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
                    final Cursor cursor = getContentResolver().query(
                            queryUri,
                            null,
                            null,
                            null,
                            null);
                    List<Movie> movies = FavoriteMovieUtils.parse(cursor);
                    if (movies != null && movies.size() > 0) {
                        mMoviesAdapter.getMovieData()
                                .add(movies.get(0));
                        mMoviesAdapter.notifyDataSetChanged();
                    }
                } else if (resultCode == FAVORITE_REMOVED) {
                    List<Movie> movies = mMoviesAdapter.getMovieData();
                    movies.remove(new Movie(movieId));
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void loadFavoriteMovies() {
        mLoadingIndicatorPb.setVisibility(View.VISIBLE);
        mMoviesAdapter.setMovieData(null);

        final Cursor cursor = getContentResolver().query(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        List<Movie> movies = FavoriteMovieUtils.parse(cursor);
        mLoadingIndicatorPb.setVisibility(View.INVISIBLE);
        mMoviesAdapter.setMovieData(movies);
    }
}
