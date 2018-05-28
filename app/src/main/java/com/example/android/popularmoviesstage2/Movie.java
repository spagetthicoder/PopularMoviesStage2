package com.example.android.popularmoviesstage2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;


import com.example.android.popularmoviesstage2.database.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Movie {
    private final String moviePoster;
    private final int movieId;
    final String MOVIEDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    public Movie(String moviePoster, int movieId) {
        this.moviePoster = moviePoster;
        this.movieId = movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getMovieId() {
        return movieId;
    }

}
