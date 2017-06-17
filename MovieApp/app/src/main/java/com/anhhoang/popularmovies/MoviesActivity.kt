/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.anhhoang.popularmovies.data.FavoriteMovieContract
import com.anhhoang.popularmovies.model.Movie
import com.anhhoang.popularmovies.model.MovieResponse
import com.anhhoang.popularmovies.utils.FavoriteMovieUtils
import com.anhhoang.popularmovies.utils.MoviesApiService
import kotlinx.android.synthetic.main.activity_movies.*

import java.net.HttpURLConnection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MoviesActivity : AppCompatActivity(), MoviesAdapter.OnMovieItemClickListener {
    private val mMoviesApiService: MoviesApiService = MoviesApiService.INSTANCE
    private val mMoviesAdapter: MoviesAdapter = MoviesAdapter(this)
    private lateinit var mLayoutManager: GridLayoutManager;
    private var mSelectedSetting: Int = 0

    private var moviesCall: Call<MovieResponse>? = null

    private val mMoviesRequestCallback = object : Callback<MovieResponse> {
        override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
            pb_loading_indicator.visibility = View.INVISIBLE
            response.body()?.results?.let { mMoviesAdapter.movieData = ArrayList(it) }

            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // TODO:
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            pb_loading_indicator.visibility = View.INVISIBLE
            t.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        val gridRowItems = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
        mSelectedSetting = R.id.action_sort_popularity

        mLayoutManager = GridLayoutManager(this, gridRowItems)

        rv_movies.layoutManager = mLayoutManager
        rv_movies.adapter = mMoviesAdapter

        if (savedInstanceState == null) {
            pb_loading_indicator.visibility = View.VISIBLE
            moviesCall = mMoviesApiService.getMoviesByPopularity()
        } else {
            mSelectedSetting = savedInstanceState.getInt(SELECTED_MENU_KEY)
            val movies = savedInstanceState.getParcelableArrayList<Movie>(ADAPTER_DATA_KEY)
            val layoutParcelable = savedInstanceState.getParcelable<Parcelable>(RECYCLER_LAYOUT_KEY)

            mMoviesAdapter.movieData = movies
            mLayoutManager.onRestoreInstanceState(layoutParcelable)
            mLayoutManager.spanCount = gridRowItems
        }
    }

    override fun onStart() {
        super.onStart()
        moviesCall?.enqueue(mMoviesRequestCallback)
    }

    override fun onStop() {
        super.onStop()
        moviesCall?.let { if (!it.isCanceled) it.cancel() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val layoutParcelable = mLayoutManager.onSaveInstanceState()
        val movies = ArrayList(mMoviesAdapter.movieData)

        outState.putParcelableArrayList(ADAPTER_DATA_KEY, movies)
        outState.putParcelable(RECYCLER_LAYOUT_KEY, layoutParcelable)
        outState.putInt(SELECTED_MENU_KEY, mSelectedSetting)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movies_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        mSelectedSetting = itemId

        // Clear up adapter's data and shows progress bar
        if (itemId == R.id.action_sort_popularity) {
            pb_loading_indicator.visibility = View.VISIBLE
            mMoviesAdapter.movieData = ArrayList()
            mMoviesApiService.getMoviesByPopularity().enqueue(mMoviesRequestCallback)
            return true
        } else if (itemId == R.id.action_sort_top_rated) {
            pb_loading_indicator.visibility = View.VISIBLE
            mMoviesAdapter.movieData = ArrayList()
            mMoviesApiService.getMoviesByTopRating().enqueue(mMoviesRequestCallback)
            return true
        } else if (itemId == R.id.action_favorite) {
            loadFavoriteMovies()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Handle result only if Favorite Setting selected
        if (mSelectedSetting == R.id.action_favorite &&
                requestCode == REQUEST_CODE &&
                resultCode != NO_ACTION_TAKEN &&
                data != null) {
            val movieId = data.getIntExtra(Intent.EXTRA_INDEX, -1)
            if (movieId != -1) {
                if (resultCode == FAVORITE_ADDED) {
                    // Get movie from persisted DB, there is any should returns only 1 result.
                    val queryUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(movieId.toString()).build()
                    val cursor = contentResolver.query(
                            queryUri, null, null, null, null)
                    val movies = FavoriteMovieUtils.parse(cursor)
                    if (movies != null && movies.isNotEmpty()) {
                        mMoviesAdapter.movieData
                                .add(movies[0])
                        mMoviesAdapter.notifyDataSetChanged()
                    }
                } else if (resultCode == FAVORITE_REMOVED) {
                    val movies = mMoviesAdapter.movieData
                    movies.remove(Movie(movieId))
                    mMoviesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onClick(movie: Movie) {
        val intent = MovieDetailActivity.createStartActivityIntent(this, movie)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun loadFavoriteMovies() {
        pb_loading_indicator.visibility = View.VISIBLE
        mMoviesAdapter.movieData = ArrayList()

        val cursor = contentResolver.query(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, null, null, null, null)
        val movies = FavoriteMovieUtils.parse(cursor)
        pb_loading_indicator.setVisibility(View.INVISIBLE)
        movies?.let { mMoviesAdapter.movieData = ArrayList(it) }
    }

    companion object {
        val FAVORITE_ADDED = 9
        val FAVORITE_REMOVED = 10
        val NO_ACTION_TAKEN = 0

        private val REQUEST_CODE = 33
        private val SELECTED_MENU_KEY = "SelectedSetting"
        private val RECYCLER_LAYOUT_KEY = "RecyclerPosition"
        private val ADAPTER_DATA_KEY = "AdapterData"
    }
}
