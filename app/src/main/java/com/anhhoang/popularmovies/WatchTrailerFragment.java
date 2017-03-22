package com.anhhoang.popularmovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class WatchTrailerFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    private static final String VIDEO_ID = "VideoId";

    private String mVideoId;

    public WatchTrailerFragment() {
        // Required empty public constructor
    }

    public static WatchTrailerFragment newInstance(String videoId) {
        WatchTrailerFragment fragment = new WatchTrailerFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_ID, videoId);
        fragment.setArguments(args);
        fragment.init();
        return fragment;
    }

    private void init() {
        mVideoId = getArguments().getString(VIDEO_ID);
        initialize(BuildConfig.YT_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (youTubePlayer == null) return;

        if (!wasRestored) {
            youTubePlayer.cueVideo(mVideoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Snackbar.make(getView(), R.string.video_load_error, Snackbar.LENGTH_LONG).show();
    }
}
