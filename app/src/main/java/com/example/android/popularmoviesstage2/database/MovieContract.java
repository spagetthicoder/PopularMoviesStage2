package com.example.android.popularmoviesstage2.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hobbit2 on 11.5.2018 г..
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_MOVIES = "movies";

        public static final String _ID ="_id";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TRAILERS = "trailers";
        public static final String COLUMN_REVIEWS = "reviews";

    }
}
