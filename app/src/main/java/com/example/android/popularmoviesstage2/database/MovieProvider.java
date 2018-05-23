package com.example.android.popularmoviesstage2.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by hobbit2 on 11.5.2018 г..
 */

public class MovieProvider extends ContentProvider {

    static final String TAG = MovieProvider.class.getSimpleName();
    static final String PROVIDER_NAME = "com.example.android.popularmoviesstage2.database.movieprovider";
    static final String URL = "content://" + PROVIDER_NAME + "/movies";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int FAVORITES = 1;
    static final int FAVORITES_ID = 2;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"movies",FAVORITES);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", FAVORITES_ID);
    }

    private SQLiteDatabase favDB;

    @Override
    public boolean onCreate() {
        Log.e(TAG,"FavoritesProvider onCreate called" );
        Context context = getContext();
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        favDB = dbHelper.getWritableDatabase();
        return (favDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MovieContract.MovieEntry.TABLE_MOVIES);

        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                Log.e(TAG,"query uriMatcher movies");
                break;
            case FAVORITES_ID: {
                Log.e(TAG, "query uriMatcher MOVIES_ID");
                qb.appendWhere(MovieContract.MovieEntry._ID + "=" +
                        uri.getLastPathSegment());
                break;
            }
        }
        Cursor cursor = qb.query (
                favDB,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.setNotificationUri(getContext().getContentResolver(),CONTENT_URI);
        return cursor;

    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /** Add a new favorites record */

        long rowID = favDB.insert(MovieContract.MovieEntry.TABLE_MOVIES, "", values);
        Log.e(TAG,"FavoritesProvider insert rowID:"+rowID);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        try {
            throw new java.sql.SQLException("Failed to add new record into "+uri);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                count = favDB.delete(MovieContract.MovieEntry.TABLE_MOVIES, selection, selectionArgs);
                break;
            case FAVORITES_ID:
                String id = uri.getPathSegments().get(1);
                count = favDB.delete(MovieContract.MovieEntry.TABLE_MOVIES,MovieContract.MovieEntry._ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
