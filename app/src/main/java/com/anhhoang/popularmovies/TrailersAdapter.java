package com.anhhoang.popularmovies;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.popularmovies.model.Trailer;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnhHo on 3/22/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    public interface OnTrailerLoadListener {
        void onPlay(String videoId);

        void onError(YouTubeInitializationResult error);
    }

    private List<Trailer> mTrailers;
    private OnTrailerLoadListener mListener;
    private boolean errorShown;

    public TrailersAdapter(OnTrailerLoadListener listener){
        if(listener == null){
            throw new IllegalArgumentException("Listener cannot be null");
        }

        mListener = listener;
    }

    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.trailer_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailersAdapter.ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        holder.itemView.setTag(trailer);

        String trailerName = String.format("%s - %s", trailer.getName(), trailer.getType());
        holder.mTrailerNameTv.setText(trailerName);

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                holder.itemView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                holder.itemView.setVisibility(View.GONE);
            }
        };

        holder.mThumbnailYttv.initialize(BuildConfig.YT_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(trailer.getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                holder.itemView.setVisibility(View.GONE);
                if (!errorShown) {
                    errorShown = true;
                    mListener.onError(youTubeInitializationResult);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers != null ? mTrailers.size() : 0;
    }

    public void setTrailers(List<Trailer> mTrailers) {
        this.mTrailers = mTrailers;
        notifyDataSetChanged();
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.yttv_thumbnail)
        public YouTubeThumbnailView mThumbnailYttv;
        @BindView(R.id.iv_play)
        public ImageView mPlayIv;
        @BindView(R.id.tv_trailer_name)
        public TextView mTrailerNameTv;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trailer trailer = (Trailer) itemView.getTag();
            mListener.onPlay(trailer.getKey());
        }
    }
}
