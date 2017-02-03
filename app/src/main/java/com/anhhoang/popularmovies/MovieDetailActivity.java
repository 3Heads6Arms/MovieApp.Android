package com.anhhoang.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = getSupportActionBar();
        ImageView backDropIv = (ImageView) findViewById(R.id.iv_backdrop);
        TextView genresTv = (TextView) findViewById(R.id.tv_genres);
        TextView statusAndReleaseDateTv = (TextView) findViewById(R.id.tv_status_release_date);
        TextView overviewTv = (TextView) findViewById(R.id.tv_overview);
    }
}
