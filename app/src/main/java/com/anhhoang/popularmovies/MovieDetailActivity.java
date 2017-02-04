package com.anhhoang.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.popularmovies.data.Genre;
import com.anhhoang.popularmovies.data.GenreResponse;
import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.data.MovieResponse;
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum;
import com.anhhoang.popularmovies.utils.MoviesApiService;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_DATA = "MovieExtraData";

    private ImageView mBackDropIv;
    private TextView mRateTv;
    private TextView mGenresTv;
    private TextView mStatusAndReleaseDateTv;
    private TextView mOverviewTv;

    private Movie mMovie;
    private Callback<GenreResponse> callback = new Callback<GenreResponse>() {
        @Override
        public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
            String genresFormatted = getFormattedGenres(mMovie.getGenreIds(), response.body().getGenres());
            mGenresTv.setText(genresFormatted);
        }

        @Override
        public void onFailure(Call<GenreResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mBackDropIv = (ImageView) findViewById(R.id.iv_backdrop);
        mGenresTv = (TextView) findViewById(R.id.tv_genres);
        mRateTv = (TextView) findViewById(R.id.tv_rate);
        mStatusAndReleaseDateTv = (TextView) findViewById(R.id.tv_release_date);
        mOverviewTv = (TextView) findViewById(R.id.tv_overview);

        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_DATA)) {
            mMovie = intent.getParcelableExtra(MOVIE_DATA);
            loadData();
        }
    }

    private void loadData() {
        ActionBar actionBar = getSupportActionBar();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String rate = String.format("%.1f|%d", mMovie.getVoteAverage(), mMovie.getVoteCount());

        actionBar.setTitle(mMovie.getTitle());

        mStatusAndReleaseDateTv.setText(dateFormat.format(mMovie.getReleaseDate()));
        mOverviewTv.setText(mMovie.getOverview());
        mRateTv.setText(rate);

        if (mMovie.getBackdropPath() != null) {
            String posterUrl = MoviesApiService.getMovieImageUrl(mMovie.getBackdropPath(), MoviePosterSizeEnum.w342);
            Glide.with(this)
                    .load(posterUrl)
                    .fitCenter()
                    .into(mBackDropIv);
        }

        MoviesApiService.getService()
                .getGenres(callback);
    }

    private String getFormattedGenres(List<Integer> genres, List<Genre> allGenres) {
        List<String> movieGenres = new ArrayList<>();
        for (Genre genre : allGenres) {
            if (genres.contains(genre.getId())) {
                movieGenres.add(genre.getName());
            }
        }

        return TextUtils.join(", ", movieGenres);
    }
}
