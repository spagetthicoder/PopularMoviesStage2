package com.example.android.popularmoviesstage2;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends Activity implements OnMovieClickListener{
    private final String TAG = DetailActivity.class.getSimpleName();
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

            MovieDetailFragment detailFragment1 = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putInt("movieId", movieId);
            args.putString("sortOrder", sortOrder);
            detailFragment1.setArguments(args);

            //detailFragment.updateContent(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.movie_list_frag, detailFragment1);
            transaction.commit();
        }
    }

    @Override
    public void onMovieClick(int movieId, int position) {
    }

    @Override
    public void onFavoriteDeleted(int movieId, int position) {
    }
}

