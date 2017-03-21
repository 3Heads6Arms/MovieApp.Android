package com.anhhoang.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anhhoang.popularmovies.model.Genre;
import com.anhhoang.popularmovies.model.GenreResponse;
import com.anhhoang.popularmovies.model.Movie;
import com.anhhoang.popularmovies.model.Review;
import com.anhhoang.popularmovies.model.ReviewResponse;
import com.anhhoang.popularmovies.utils.CustomLinearSnapHelper;
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum;
import com.anhhoang.popularmovies.utils.MoviesApiService;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_DATA = "MovieExtraData";

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

    private GenresAdapter mGenresAdapter;

    private ReviewsAdapter mReviewsAdapter;

    private Movie mMovie;
    private MoviesApiService mMoviesService;

    private CustomLinearSnapHelper.OnLinearSnapFling mOnSnapFling = new CustomLinearSnapHelper.OnLinearSnapFling() {
        @Override
        public void onFling() {
            mDetailSv.smoothScrollTo(0, mReviewsRv.getTop());
        }
    };
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

    private Callback<ReviewResponse> reviewsCallback = new Callback<ReviewResponse>() {
        @Override
        public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
            List<Review> reviews = response.body().getResults();
            int totalPages = response.body().getTotalPages();
            int currentPage = response.body().getPage();
            int totalReviews = response.body().getTotalResults();

            if(totalReviews > 0){
                mReviewsRv.setVisibility(View.VISIBLE);
                mReviewsLabelTv.setVisibility(View.VISIBLE);
            } else {
                mReviewsRv.setVisibility(View.INVISIBLE);
                mReviewsLabelTv.setVisibility(View.INVISIBLE);
            }

            mReviewsAdapter.setTotalPages(totalPages);
            mReviewsAdapter.setTotalReviews(totalReviews);
            mReviewsAdapter.setCurrentPage(currentPage);
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
        mMoviesService = MoviesApiService.getService();
        Intent intent = getIntent();

        mGenresAdapter = new GenresAdapter();
        mGenresRv.setAdapter(mGenresAdapter);

        mReviewsRv.setVisibility(View.INVISIBLE);
        mReviewsLabelTv.setVisibility(View.INVISIBLE);

        CustomLinearSnapHelper linearSnapHelper = new CustomLinearSnapHelper();
        linearSnapHelper.setOnSnapFling(mOnSnapFling);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRv.setAdapter(mReviewsAdapter);
        linearSnapHelper.attachToRecyclerView(mReviewsRv);

        if (intent.hasExtra(MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MOVIE_DATA);
            loadData();
        }
    }

    // Extracted data loading for better code reading
    private void loadData() {
        ActionBar actionBar = getSupportActionBar();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String rate = String.format("%.1f|%d", mMovie.getVoteAverage(), mMovie.getVoteCount());

        actionBar.setTitle(mMovie.getTitle());

        mReleaseDateTv.setText(dateFormat.format(mMovie.getReleaseDate()));
        mOverviewTv.setText(mMovie.getOverview());
        mRateTv.setText(rate);

        if (mMovie.getBackdropPath() != null) {
            String backdropUrl = MoviesApiService.getMovieImageUrl(mMovie.getBackdropPath(), MoviePosterSizeEnum.w342);
            Glide.with(this)
                    .load(backdropUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(mBackDropIv);
        }

        if (mMovie.getPosterPath() != null) {
            String posterUrl = MoviesApiService.getMovieImageUrl(mMovie.getPosterPath(), MoviePosterSizeEnum.w92);

            Glide.with(this)
                    .load(posterUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(mPosterIv);
        }

        mMoviesService.getGenres(genresCallback);
        mMoviesService.getReviews(mMovie.getId(), mReviewsAdapter.getCurrentPage() + 1, reviewsCallback);

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
