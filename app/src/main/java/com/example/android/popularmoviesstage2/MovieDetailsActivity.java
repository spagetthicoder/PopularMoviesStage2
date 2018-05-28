package com.example.android.popularmoviesstage2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class MovieDetailsActivity extends Activity implements OnMovieClickListener{
    private final String TAG = MovieDetailsActivity.class.getSimpleName();
    private int movieId;
    private String sortOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_movie_details);
        Intent detailIntent = getIntent();

        Log.e(TAG,"MovieDetailActivity entered");
        movieId=detailIntent.getIntExtra("MovieListItem",movieId);

        sortOrder = detailIntent.getStringExtra("SortOrder");
        Log.e(TAG, "MovieDetailActivity movieId:"+movieId+" "+"sortOrder:"+sortOrder);

        if( savedInstanceState == null ) {
            MovieDetailFragment detailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_list_frag);
            Bundle args = new Bundle();
            args.putInt("movieId", movieId);
            args.putString("sortOrder", sortOrder);
            detailFragment.updateContent(args);
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMovieClick(int movieId, int position) {
    }

    @Override
    public void onFavoriteDeleted(int movieId, int position) {
    }
}

