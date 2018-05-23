package com.example.android.popularmoviesstage2;

public interface OnMovieClickListener {
    void onMovieClick(int movieId, int position);
    void onFavoriteDeleted(int movieId, int position);
}