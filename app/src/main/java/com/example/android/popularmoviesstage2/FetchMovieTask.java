package com.example.android.popularmoviesstage2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FetchMovieTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

    private final MovieAdapter adapter;
    private final String sortOrder;
    private final FetchMovieTaskListener fetchListener;
    private static final String TAG = FetchMovieTask.class.getSimpleName();
    JSONObject jObj;
    final String MOVIEDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    FetchMovieTask(MovieAdapter adapter, String sortOrder, FetchMovieTaskListener fetchMovieTaskListener){
        this.sortOrder = sortOrder;
        this.adapter = adapter;
        this.fetchListener = fetchMovieTaskListener;
    }
    @Override
    protected ArrayList<Movie> doInBackground(Integer... params) {
        ArrayList<Movie> moviePosters = new ArrayList<>();

        try{
            if(sortOrder.equals("popularity.desc")) {
                jObj = JSONLoader.load("/movie/popular?");
            }else
            {
                 jObj = JSONLoader.load("/movie/top_rated?");
            }
            if(jObj == null) {
                Log.v(TAG, "Can not load the data from remote service");
                return null;
            }
            JSONArray movieArray = jObj.getJSONArray("results");

            for(int i=0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.optJSONObject(i);
                String moviePoster = movie.getString("poster_path");
                int movieId = movie.getInt("id");
                Movie data = new Movie(MOVIEDB_POSTER_BASE_URL + moviePoster, movieId);
                moviePosters.add(data);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error:",e);
        }
        return moviePosters;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviePosters) {
        super.onPostExecute(moviePosters);
        if (moviePosters != null)
        {
            for (Movie res : moviePosters)
            {
                adapter.add(res);
            }
            adapter.notifyDataSetChanged();
            fetchListener.onFetchCompleted();
        } else {
            fetchListener.onFetchFailed();

        }
    }
}
