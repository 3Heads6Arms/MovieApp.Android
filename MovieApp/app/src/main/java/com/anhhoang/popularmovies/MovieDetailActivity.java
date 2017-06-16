package com.anhhoang.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anhhoang.popularmovies.data.FavoriteMovieContract;
import com.anhhoang.popularmovies.model.Genre;
import com.anhhoang.popularmovies.model.GenreResponse;
import com.anhhoang.popularmovies.model.Movie;
import com.anhhoang.popularmovies.model.Review;
import com.anhhoang.popularmovies.model.ReviewResponse;
import com.anhhoang.popularmovies.model.Trailer;
import com.anhhoang.popularmovies.model.TrailerResponse;
import com.anhhoang.popularmovies.utils.CustomLinearSnapHelper;
import com.anhhoang.popularmovies.utils.FavoriteMovieUtils;
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum;
import com.anhhoang.popularmovies.utils.MoviesApiService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_DATA = "MovieExtraData";

    private static final String MOVIE_DATA_KEY = "MovieSaveData";
    private static final String FAVORITE_ACTION_TAKEN_KEY = "FavoriteActionTaken";
    private static final String ORIGINAL_FAVORITE_STATE_KEY = "FavoriteState";
    private static final String SCROLL_POSITION_KEY = "ScrollPosition";
    private static final String GENRE_ADAPTER_DATA_KEY = "GenreAdapterData";
    private static final String GENRE_LAYOUT_KEY = "GenreLayout";
    private static final String TRAILER_ADAPTER_DATA_KEY = "TrailerAdapterData";
    private static final String TRAILER_LAYOUT_KEY = "TrailerLayout";
    private static final String REVIEW_COUNT_KEY = "ReviewCount";
    private static final String REVIEW_ADAPTER_DATA_KEY = "ReviewAdapterData";
    private static final String REVIEW_LAYOUT_KEY = "ReviewLayout";
    private static final String BACKDROP_IMAGE_KEY = "BackDropImage";
    private static final String POSTER_IMAGE_KEY = "PosterImage";

    @BindView(R.id.iv_backdrop)
    ImageView mBackDropIv;
    @BindView(R.id.iv_poster)
    ImageView mPosterIv;
    @BindView(R.id.tv_rate)
    TextView mRateTv;
    @BindView(R.id.rv_genres)
    RecyclerView mGenresRv;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTv;
    @BindView(R.id.tv_overview)
    TextView mOverviewTv;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRv;
    @BindView(R.id.sv_detail)
    ScrollView mDetailSv;
    @BindView(R.id.tv_reviews_label)
    TextView mReviewsLabelTv;
    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersRv;
    @BindView(R.id.fab_favorite)
    FloatingActionButton mFavoriteFab;

    private GenresAdapter mGenresAdapter;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private Movie mMovie;
    private MoviesApiService mMoviesService;
    private int mFavoriteActionTaken;
    // Used to compare with final state to determine if it was changed;
    // User may tap favorite button few times, causes indetermination of actual favorite state of the movie, while mFavoriteActionTaken still get changes.
    // If user state is returned to original state, parent activity doesn't have to handle it
    private boolean mOriginalFavoriteState;

    private Callback<GenreResponse> genresCallback = new Callback<GenreResponse>() {
        @Override
        public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
            List<Genre> genres = getMovieGenres(mMovie.getGenreIds(), response.body().getGenres());
            mGenresAdapter.setGenres(genres);
        }

        @Override
        public void onFailure(Call<GenreResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };
    private Callback<TrailerResponse> trailersCallback = new Callback<TrailerResponse>() {
        @Override
        public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
            mTrailersAdapter.setTrailers(response.body().getResults());
        }

        @Override
        public void onFailure(Call<TrailerResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };
    private Callback<ReviewResponse> reviewsCallback = new Callback<ReviewResponse>() {
        @Override
        public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
            List<Review> reviews = response.body().getResults();
            int totalReviews = response.body().getTotalResults();

            if (totalReviews > 0) {
                mReviewsRv.setVisibility(View.VISIBLE);
                mReviewsLabelTv.setVisibility(View.VISIBLE);
            } else {
                mReviewsRv.setVisibility(View.INVISIBLE);
                mReviewsLabelTv.setVisibility(View.INVISIBLE);
            }

            mReviewsAdapter.setTotalReviews(totalReviews);
            mReviewsAdapter.setReviews(reviews);
        }

        @Override
        public void onFailure(Call<ReviewResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMoviesService = MoviesApiService.getService();

        mGenresAdapter = new GenresAdapter();
        mGenresRv.setAdapter(mGenresAdapter);

        mReviewsRv.setVisibility(View.INVISIBLE);
        mReviewsLabelTv.setVisibility(View.INVISIBLE);

        CustomLinearSnapHelper linearSnapHelper = new CustomLinearSnapHelper();
        linearSnapHelper.setOnSnapFling(new CustomLinearSnapHelper.OnLinearSnapFling() {
            @Override
            public void onFling() {
                mDetailSv.smoothScrollTo(0, mReviewsRv.getTop());
            }
        });
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRv.setAdapter(mReviewsAdapter);
        linearSnapHelper.attachToRecyclerView(mReviewsRv);

        mTrailersAdapter = new TrailersAdapter(new TrailersAdapter.OnTrailerLoadListener() {
            @Override
            public void onPlay(String videoId) {
                //new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                // Updated, so it can play from the app its own
                Intent trailerIntent = YouTubeStandalonePlayer.createVideoIntent(
                        MovieDetailActivity.this,
                        BuildConfig.YT_API_KEY,
                        videoId);
                startActivity(trailerIntent);
            }

            @Override
            public void onError(YouTubeInitializationResult error) {
                error.getErrorDialog(MovieDetailActivity.this, 0).show();
            }
        });
        mTrailersRv.setHasFixedSize(true);
        mTrailersRv.setAdapter(mTrailersAdapter);


        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (!intent.hasExtra(MOVIE_DATA))
                throw new IllegalArgumentException("Missing Movie extra");
            mMovie = intent.getParcelableExtra(MOVIE_DATA);
            mFavoriteActionTaken = MoviesActivity.NO_ACTION_TAKEN;
            setSimpleMovieValues();
            // Loaded original state after setSimpleMovieValues, because movie values update favorite state from persisted data.
            mOriginalFavoriteState = mMovie.isUserFavorite();
            // Load More complex data.
            // Extracted incase we need to customely handle state changes, this should not be executed, but to load from state
            loadData();
        } else {
            loadSavedInstanceData(savedInstanceState);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        List<YouTubeThumbnailLoader> loaders = mTrailersAdapter.getLoaders();
        for (YouTubeThumbnailLoader loader : loaders) {
            loader.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finalizeFavoriteResult();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finalizeFavoriteResult();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_DATA_KEY, mMovie);
        outState.putInt(SCROLL_POSITION_KEY, mDetailSv.getScrollY());
        outState.putParcelableArrayList(
                GENRE_ADAPTER_DATA_KEY,
                new ArrayList<>(mGenresAdapter.getGenres()));
        outState.putParcelable(
                GENRE_LAYOUT_KEY,
                mGenresRv.getLayoutManager()
                        .onSaveInstanceState());
        outState.putParcelableArrayList(
                TRAILER_ADAPTER_DATA_KEY,
                new ArrayList<>(mTrailersAdapter.getTrailers()));
        outState.putParcelable(
                TRAILER_LAYOUT_KEY,
                mTrailersRv
                        .getLayoutManager()
                        .onSaveInstanceState());
        outState.putParcelableArrayList(
                REVIEW_ADAPTER_DATA_KEY,
                new ArrayList<>(mReviewsAdapter.getReviews()));
        outState.putParcelable(
                REVIEW_LAYOUT_KEY,
                mReviewsRv
                        .getLayoutManager()
                        .onSaveInstanceState());
        outState.putInt(FAVORITE_ACTION_TAKEN_KEY, mFavoriteActionTaken);
        outState.putBoolean(ORIGINAL_FAVORITE_STATE_KEY, mOriginalFavoriteState);
        outState.putInt(REVIEW_COUNT_KEY, mReviewsAdapter.getTotalReviews());
    }

    @OnClick(R.id.fab_favorite)
    public void fab_favoriteClick() {
        mMovie.setUserFavorite(!mMovie.isUserFavorite());
        int favoriteIconId = mMovie.isUserFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp;
        mFavoriteFab.setImageResource(favoriteIconId);
        if (mMovie.isUserFavorite()) {
            mFavoriteActionTaken = MoviesActivity.FAVORITE_ADDED;
            // Add new favorite to realm if isn't already exists in realm
            ContentValues contentValues = FavoriteMovieUtils.parse(mMovie);
            Uri result = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);
        } else {
            // Movie is no longer favorite, remove from realm
            mFavoriteActionTaken = MoviesActivity.FAVORITE_REMOVED;
            Uri deleteUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();
            int deleted = getContentResolver().delete(
                    deleteUri,
                    null,
                    null
            );
        }
    }

    private void loadSavedInstanceData(Bundle savedInstanceState) {
        mMovie = savedInstanceState.getParcelable(MOVIE_DATA_KEY);
        mFavoriteActionTaken = savedInstanceState.getInt(FAVORITE_ACTION_TAKEN_KEY);
        mOriginalFavoriteState = savedInstanceState.getBoolean(ORIGINAL_FAVORITE_STATE_KEY);

        int scrollY = savedInstanceState.getInt(SCROLL_POSITION_KEY);
        List<Genre> genres = savedInstanceState.getParcelableArrayList(GENRE_ADAPTER_DATA_KEY);
        Parcelable genresParcel = savedInstanceState.getParcelable(GENRE_LAYOUT_KEY);
        List<Trailer> trailers = savedInstanceState.getParcelableArrayList(TRAILER_ADAPTER_DATA_KEY);
        Parcelable trailersParcel = savedInstanceState.getParcelable(TRAILER_LAYOUT_KEY);
        List<Review> reviews = savedInstanceState.getParcelableArrayList(REVIEW_ADAPTER_DATA_KEY);
        Parcelable reviewsParcel = savedInstanceState.getParcelable(REVIEW_LAYOUT_KEY);
        int totalReviews = savedInstanceState.getInt(REVIEW_COUNT_KEY);

        setSimpleMovieValues();

        mGenresAdapter.setGenres(genres);
        mGenresRv.getLayoutManager().onRestoreInstanceState(genresParcel);
        mTrailersAdapter.setTrailers(trailers);
        mTrailersRv.getLayoutManager().onRestoreInstanceState(trailersParcel);

        if (totalReviews > 0) {
            mReviewsRv.setVisibility(View.VISIBLE);
            mReviewsLabelTv.setVisibility(View.VISIBLE);
        } else {
            mReviewsRv.setVisibility(View.INVISIBLE);
            mReviewsLabelTv.setVisibility(View.INVISIBLE);
        }
        mReviewsAdapter.setTotalReviews(totalReviews);
        mReviewsAdapter.setReviews(reviews);
        mReviewsRv.getLayoutManager().onRestoreInstanceState(reviewsParcel);
    }

    private void finalizeFavoriteResult() {
        if (mFavoriteActionTaken == MoviesActivity.NO_ACTION_TAKEN || mMovie.isUserFavorite() == mOriginalFavoriteState) {
            setResult(MoviesActivity.NO_ACTION_TAKEN);
        } else {
            Intent data = new Intent();
            data.putExtra(Intent.EXTRA_INDEX, mMovie.getId());
            setResult(mFavoriteActionTaken, data);
        }

        finish();
    }

    // Extracted data loading for better code reading
    private void loadData() {
        mMoviesService.getGenres(genresCallback);
        mMoviesService.getTrailers(mMovie.getId(), trailersCallback);
        mMoviesService.getReviews(mMovie.getId(), mReviewsAdapter.getCurrentPage() + 1, reviewsCallback);

    }

    private void setSimpleMovieValues() {
        ActionBar actionBar = getSupportActionBar();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String rate = String.format("%.1f|%d", mMovie.getVoteAverage(), mMovie.getVoteCount());
        // Get favorite status for the movie (movies source might not come from DB)
        Uri queryUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();
        final Cursor cursor = getContentResolver().query(
                queryUri,
                null,
                null,
                null,
                null);
        List<Movie> movies = FavoriteMovieUtils.parse(cursor);
        if (movies != null) {
            boolean isUserFavorite = movies.get(0).isUserFavorite();
            mMovie.setUserFavorite(isUserFavorite);
        }

        int favoriteIconId = mMovie.isUserFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp;

        actionBar.setTitle(mMovie.getTitle());

        mReleaseDateTv.setText(dateFormat.format(mMovie.getReleaseDate()));
        mOverviewTv.setText(mMovie.getOverview());
        mRateTv.setText(rate);
        mFavoriteFab.setImageResource(favoriteIconId);

        if (mMovie.getBackdropPath() != null) {
            String backdropUrl = MoviesApiService.getMovieImageUrl(mMovie.getBackdropPath(), MoviePosterSizeEnum.w342);
            Glide.with(this)
                    .load(backdropUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(mBackDropIv);
        }

        if (mMovie.getPosterPath() != null) {
            String posterUrl = MoviesApiService.getMovieImageUrl(mMovie.getPosterPath(), MoviePosterSizeEnum.w92);
            Glide.with(this)
                    .load(posterUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(mPosterIv);
        }
    }

    private List<Genre> getMovieGenres(List<Integer> genres, List<Genre> allGenres) {
        List<Genre> movieGenres = new ArrayList<>();
        for (Genre genre : allGenres) {
            if (genres.contains(genre.getId())) {
                movieGenres.add(genre);
            }
        }

        return movieGenres;
    }
}
