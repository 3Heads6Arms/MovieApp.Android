package com.anhhoang.popularmovies;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.popularmovies.model.Genre;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnhHo on 3/20/2017.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private List<Genre> mGenres;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attachImmediatelyToRoot = false;
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.genre_item_view, parent, attachImmediatelyToRoot);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = mGenres.get(position);
        holder.genreTv.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return mGenres != null ? mGenres.size() : 0;
    }

    public void setGenres(List<Genre> genres) {
        this.mGenres = genres;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_genre)
        public TextView genreTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
