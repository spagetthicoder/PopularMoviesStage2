package com.example.android.popularmoviesstage2;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesstage2.database.MovieContract;
import com.example.android.popularmoviesstage2.database.MovieProvider;

import java.util.ArrayList;

public class FetchFavoriteMovieTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private static final String TAG = FetchFavoriteMovieTask.class.getSimpleName();
    private final MovieAdapter adapter;
    private final ContentResolver contentResolver;

    FetchFavoriteMovieTask(MovieAdapter adapter, ContentResolver contentResolver) {
        this.adapter = adapter;
        this.contentResolver = contentResolver;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> moviePosters = new ArrayList<>();
        String[] projection = new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.COLUMN_POSTER};
        final Cursor cursor = contentResolver.query(MovieProvider.CONTENT_URI,projection,null,null,null);
        Log.d(TAG,"Cursor count:"+cursor.getCount() );
        if (cursor.getCount()!=0) {
            Log.e(TAG,"cursor column count:"+cursor.getColumnCount());
            while(cursor.moveToNext()) {
                Movie data = new Movie(cursor.getString(1), cursor.getInt(0));
                moviePosters.add(data);
            }
        }
        return moviePosters;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviePosters) {
        super.onPostExecute(moviePosters);
        if (moviePosters!=null) {
            for (Movie res : moviePosters) {
                adapter.add(res);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
