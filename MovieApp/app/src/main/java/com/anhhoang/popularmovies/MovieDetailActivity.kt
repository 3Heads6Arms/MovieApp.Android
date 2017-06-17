package com.anhhoang.popularmovies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View

import com.anhhoang.popularmovies.data.FavoriteMovieContract
import com.anhhoang.popularmovies.model.Genre
import com.anhhoang.popularmovies.model.GenreResponse
import com.anhhoang.popularmovies.model.Movie
import com.anhhoang.popularmovies.model.Review
import com.anhhoang.popularmovies.model.ReviewResponse
import com.anhhoang.popularmovies.model.Trailer
import com.anhhoang.popularmovies.model.TrailerResponse
import com.anhhoang.popularmovies.utils.CustomLinearSnapHelper
import com.anhhoang.popularmovies.utils.FavoriteMovieUtils
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum
import com.anhhoang.popularmovies.utils.MoviesApiService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_movie_detail.*

import java.text.SimpleDateFormat
import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity(), TrailersAdapter.OnTrailerClickListener {

    private lateinit var mMovie: Movie
    private val mMoviesService: MoviesApiService = MoviesApiService.INSTANCE
    private var mFavoriteActionTaken: Int = 0
    // Used to compare with final state to determine if it was changed;
    // User may tap favorite button few times, causes indetermination of actual favorite state of the movie, while mFavoriteActionTaken still get changes.
    // If user state is returned to original state, parent activity doesn't have to handle it
    private var mOriginalFavoriteState: Boolean = false

    private val mGenreAdapter = GenresAdapter()
    private val mReviewAdapter = ReviewsAdapter()
    private val mTrailerAdapter = TrailersAdapter(this)

    private var mGenreCall: Call<GenreResponse>? = null
    private var mReviewCall: Call<ReviewResponse>? = null
    private var mTrailerCall: Call<TrailerResponse>? = null

    private val genresCallback = object : Callback<GenreResponse> {
        override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
            val genres = getMovieGenres(mMovie.genreIds, response.body()?.genres)
            mGenreAdapter.genres = genres

            mGenreCall = null
        }

        override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    private val reviewsCallback = object : Callback<ReviewResponse> {
        override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
            val reviews = response.body()?.results
            val totalReviews = response.body()?.totalResults
            val totalPages = response.body()?.totalPages

            reviews?.let { mReviewAdapter.reviews = it }
            totalReviews?.let { mReviewAdapter.totalReviews = it }
            totalPages?.let { mReviewAdapter.totalPages = it }


            if (mReviewAdapter.totalReviews > 0) {
                rv_reviews.visibility = View.VISIBLE
                tv_reviews_label.visibility = View.VISIBLE
            } else {
                rv_reviews.visibility = View.INVISIBLE
                tv_reviews_label.visibility = View.INVISIBLE
            }

            mReviewCall = null
        }

        override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    private val trailersCallback = object : Callback<TrailerResponse> {
        override fun onFailure(call: Call<TrailerResponse>?, t: Throwable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onResponse(call: Call<TrailerResponse>?, response: Response<TrailerResponse>?) {
            val trailers = response?.body()?.results

            trailers?.let { mTrailerAdapter.trailerData = it }

            mTrailerCall = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val linearSnapHelper = CustomLinearSnapHelper()
        linearSnapHelper.setOnSnapFling(object : CustomLinearSnapHelper.OnLinearSnapFling {
            override fun onFling() = sv_detail.smoothScrollTo(0, rv_reviews.getTop())
        })

        rv_genres.adapter = mGenreAdapter
        rv_reviews.adapter = mReviewAdapter
        rv_trailers.adapter = mTrailerAdapter

        rv_trailers.setHasFixedSize(true)
        rv_reviews.visibility = View.INVISIBLE
        tv_reviews_label.visibility = View.INVISIBLE
        linearSnapHelper.attachToRecyclerView(rv_reviews)

        fab_favorite.setOnClickListener {
            mMovie.isUserFavorite = !mMovie.isUserFavorite
            val favoriteIconId = if (mMovie.isUserFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
            fab_favorite.setImageResource(favoriteIconId)
            if (mMovie.isUserFavorite) {
                mFavoriteActionTaken = MoviesActivity.FAVORITE_ADDED
                // Add new favorite to realm if isn't already exists in realm

                val contentValues = FavoriteMovieUtils.parse(mMovie)
                contentResolver.insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues)
            } else {
                // Movie is no longer favorite, remove from realm
                mFavoriteActionTaken = MoviesActivity.FAVORITE_REMOVED
                val deleteUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(mMovie.id.toString()).build()
                contentResolver.delete(deleteUri, null, null)
            }
        }

        if (savedInstanceState == null) {
            if (!intent.hasExtra(MOVIE_DATA)) {
                throw IllegalArgumentException("Missing Movie extra")
            }

            mMovie = intent.getParcelableExtra<Movie>(MOVIE_DATA)
            mFavoriteActionTaken = MoviesActivity.NO_ACTION_TAKEN
            setSimpleMovieValues()
            // Loaded original state after setSimpleMovieValues, because movie values update favorite state from persisted data.
            mOriginalFavoriteState = mMovie.isUserFavorite
            // Load More complex data.
            // Extracted incase we need to customely handle state changes, this should not be executed, but to load from state

            mGenreCall = mMoviesService.getGenres()
            mTrailerCall = mMoviesService.getTrailers(mMovie.id)
            mReviewCall = mMoviesService.getReviews(mMovie.id, mReviewAdapter?.currentPage + 1)
        } else {
            loadSavedInstanceData(savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        mGenreCall?.enqueue(genresCallback)
        mTrailerCall?.enqueue(trailersCallback)
        mReviewCall?.enqueue(reviewsCallback)

        setSimpleMovieValues()
    }

    override fun onStop() {
        super.onStop()
        mGenreCall?.let { if (!it.isCanceled) it.cancel() }
        mTrailerCall?.let { if (!it.isCanceled) it.cancel() }
        mReviewCall?.let { if (!it.isCanceled) it.cancel() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finalizeFavoriteResult()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finalizeFavoriteResult()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(MOVIE_DATA_KEY, mMovie)
        outState.putInt(SCROLL_POSITION_KEY, sv_detail.getScrollY())
        outState.putParcelableArrayList(
                GENRE_ADAPTER_DATA_KEY,
                ArrayList(mGenreAdapter.genres))
        outState.putParcelable(
                GENRE_LAYOUT_KEY,
                rv_genres.getLayoutManager()
                        .onSaveInstanceState())
        outState.putParcelableArrayList(
                TRAILER_ADAPTER_DATA_KEY,
                ArrayList(mTrailerAdapter.trailerData))
        outState.putParcelable(
                TRAILER_LAYOUT_KEY,
                rv_trailers
                        .getLayoutManager()
                        .onSaveInstanceState())
        outState.putParcelableArrayList(
                REVIEW_ADAPTER_DATA_KEY,
                ArrayList(mReviewAdapter.reviews))
        outState.putParcelable(
                REVIEW_LAYOUT_KEY,
                rv_reviews
                        .getLayoutManager()
                        .onSaveInstanceState())
        outState.putInt(FAVORITE_ACTION_TAKEN_KEY, mFavoriteActionTaken)
        outState.putBoolean(ORIGINAL_FAVORITE_STATE_KEY, mOriginalFavoriteState)
        outState.putInt(REVIEW_COUNT_KEY, mReviewAdapter.totalReviews)
    }

    override fun onClick(trailerKey: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(MoviesApiService.YOUTUBE_URL + trailerKey))
        startActivity(intent)
    }

    private fun loadSavedInstanceData(savedInstanceState: Bundle) {
        mMovie = savedInstanceState.getParcelable<Movie>(MOVIE_DATA_KEY)
        mFavoriteActionTaken = savedInstanceState.getInt(FAVORITE_ACTION_TAKEN_KEY)
        mOriginalFavoriteState = savedInstanceState.getBoolean(ORIGINAL_FAVORITE_STATE_KEY)

        val scrollY = savedInstanceState.getInt(SCROLL_POSITION_KEY)
        val genres = savedInstanceState.getParcelableArrayList<Genre>(GENRE_ADAPTER_DATA_KEY)
        val genresParcel = savedInstanceState.getParcelable<Parcelable>(GENRE_LAYOUT_KEY)
        val trailers = savedInstanceState.getParcelableArrayList<Trailer>(TRAILER_ADAPTER_DATA_KEY)
        val trailersParcel = savedInstanceState.getParcelable<Parcelable>(TRAILER_LAYOUT_KEY)
        val reviews = savedInstanceState.getParcelableArrayList<Review>(REVIEW_ADAPTER_DATA_KEY)
        val reviewsParcel = savedInstanceState.getParcelable<Parcelable>(REVIEW_LAYOUT_KEY)
        val totalReviews = savedInstanceState.getInt(REVIEW_COUNT_KEY)

        setSimpleMovieValues()

        mGenreAdapter.genres = genres
        rv_genres.layoutManager.onRestoreInstanceState(genresParcel)
        mTrailerAdapter.trailerData = trailers
        rv_trailers.layoutManager.onRestoreInstanceState(trailersParcel)


        if (mReviewAdapter.totalReviews > 0) {
            rv_reviews.visibility = View.VISIBLE
            tv_reviews_label.visibility = View.VISIBLE
        } else {
            rv_reviews.visibility = View.INVISIBLE
            tv_reviews_label.visibility = View.INVISIBLE
        }
        mReviewAdapter.totalReviews = totalReviews
        mReviewAdapter.reviews = reviews
        rv_reviews.layoutManager.onRestoreInstanceState(reviewsParcel)
        sv_detail.scrollY = scrollY
    }

    private fun finalizeFavoriteResult() {
        if (mFavoriteActionTaken == MoviesActivity.NO_ACTION_TAKEN || mMovie.isUserFavorite == mOriginalFavoriteState) {
            setResult(MoviesActivity.NO_ACTION_TAKEN)
        } else {
            val data = Intent()
            data.putExtra(Intent.EXTRA_INDEX, mMovie.id)
            setResult(mFavoriteActionTaken, data)
        }

        finish()
    }


    private fun setSimpleMovieValues() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val rate = String.format("%.1f|%d", mMovie.voteAverage, mMovie.voteCount)
        // Get favorite status for the movie (movies source might not come from DB)
        val queryUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(mMovie.id.toString()).build()
        val cursor = contentResolver.query(
                queryUri, null, null, null, null)
        val movies = FavoriteMovieUtils.parse(cursor)
        movies?.let {
            val isUserFavorite = it[0].isUserFavorite
            mMovie.isUserFavorite = isUserFavorite
        }

        val favoriteIconId = if (mMovie.isUserFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp

        supportActionBar?.setTitle(mMovie.title)

        tv_release_date.setText(dateFormat.format(mMovie.releaseDate))
        tv_overview.setText(mMovie.overview)
        tv_rate.setText(rate)
        fab_favorite.setImageResource(favoriteIconId)

        mMovie.backdropPath?.let {
            val backdropUrl = MoviesApiService.getMovieImageUrl(it, MoviePosterSizeEnum.w342)
            Glide.with(this)
                    .load(backdropUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(iv_backdrop)
        }

        mMovie.posterPath?.let {
            val posterUrl = MoviesApiService.getMovieImageUrl(it, MoviePosterSizeEnum.w92)
            Glide.with(this)
                    .load(posterUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(iv_poster)
        }
    }

    private fun getMovieGenres(genres: List<Int>?, allGenres: List<Genre>?): List<Genre> {
        val movieGenres = ArrayList<Genre>()
        allGenres?.let {
            for (genre in it) {
                if (genres?.contains(genre.id) == true) {
                    movieGenres.add(genre)
                }
            }
        }

        return movieGenres
    }

    companion object {
        private val MOVIE_DATA = "MovieExtraData"

        private val MOVIE_DATA_KEY = "MovieSaveData"
        private val FAVORITE_ACTION_TAKEN_KEY = "FavoriteActionTaken"
        private val ORIGINAL_FAVORITE_STATE_KEY = "FavoriteState"
        private val SCROLL_POSITION_KEY = "ScrollPosition"
        private val GENRE_ADAPTER_DATA_KEY = "GenreAdapterData"
        private val GENRE_LAYOUT_KEY = "GenreLayout"
        private val TRAILER_ADAPTER_DATA_KEY = "TrailerAdapterData"
        private val TRAILER_LAYOUT_KEY = "TrailerLayout"
        private val REVIEW_COUNT_KEY = "ReviewCount"
        private val REVIEW_ADAPTER_DATA_KEY = "ReviewAdapterData"
        private val REVIEW_LAYOUT_KEY = "ReviewLayout"

        fun createStartActivityIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_DATA, movie)

            return intent
        }
    }
}
