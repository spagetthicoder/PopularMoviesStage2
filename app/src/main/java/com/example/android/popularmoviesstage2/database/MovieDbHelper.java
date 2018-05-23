package com.example.android.popularmoviesstage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hobbit2 on 11.5.2018 г..
 */

public class MovieDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 3;

    private SQLiteDatabase mDB;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_MOVIES + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieContract.MovieEntry.COLUMN_TITLE + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_POSTER + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE," +
                    MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_TRAILERS + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_REVIEWS + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(LOG_TAG, "Upgrading database from version " + i + " to " +
                i1 + ". OLD DATA WILL BE DESTROYED");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIES);
        onCreate(sqLiteDatabase);
    }
}
