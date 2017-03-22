package com.anhhoang.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


public class WatchTrailerActivity extends AppCompatActivity {
    public static String VIDEO_KEY_EXTRA = "VideoId";


    private String mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_trailer);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(VIDEO_KEY_EXTRA)) {
            throw new UnsupportedOperationException("Intent or Video Id is not available");
        }

        mVideoId = intent.getStringExtra(VIDEO_KEY_EXTRA);

        Fragment fragment = WatchTrailerFragment.newInstance(mVideoId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, WatchTrailerFragment.class.getSimpleName())
                .commit();
    }
}
