package com.example.android.popularmoviesstage2;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements OnMovieClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean loadingState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);
        getSortMethod();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        MovieDetailFragment detailFrag = (MovieDetailFragment)getFragmentManager().findFragmentById(R.id.detail_frag);
        if (detailFrag != null) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(detailFrag);
            ft.attach(detailFrag);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popularity:
                updateSharedPreferences(getString(R.string.highest_rated_value));
                refreshFragmment();
                return true;
            case R.id.topRated:
                updateSharedPreferences(getString(R.string.most_popular_value));
                refreshFragmment();
                return true;
            case R.id.favorites:
                updateSharedPreferences(getString(R.string.favorites_value));
                refreshFragmment();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFragmment() {
        MoviesListFragment fragment = new MoviesListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.movie_list_frag, fragment);
        transaction.commit();
    }

    private String getSortMethod() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        return preferences.getString(getString(R.string.preferenceSortMethodKey), getString(R.string.movieSortPopularity));
    }


    private void updateSharedPreferences(String sortMethod) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //String value = sharedPreferences.getString(MainActivity.SHARED_PREF_KEY,getString(R.string.most_popular_value));

        editor.putString(getString(R.string.preferenceSortMethodKey),sortMethod);
        editor.commit();
        return;
    }

    @Override
    public void onMovieClick(int movieId, int position) {
        MovieDetailFragment detailFrag = (MovieDetailFragment)getFragmentManager().findFragmentById(R.id.detail_frag);
        if (detailFrag == null) {
            Log.v(TAG, "onMovieClick movieId :" + movieId);
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra("MovieListItem",movieId);
            detailIntent.putExtra("SortOrder", getSortMethod());
            detailIntent.putExtra("position", position);
            startActivity(detailIntent);
        } else {
            Bundle args = new Bundle();
            args.putInt("movieId", movieId);
            args.putString("sortOrder", getSortMethod());
            args.putInt("position", position);
            detailFrag.updateContent(args);
        }
    }

    @Override
    public void onFavoriteDeleted(int movieId, int position) {
        MovieDetailFragment detailFrag = (MovieDetailFragment)getFragmentManager().findFragmentById(R.id.detail_frag);
        if (detailFrag != null) {
            //update the movie list fragment
            MoviesListFragment listFrag = (MoviesListFragment)getFragmentManager().findFragmentById(R.id.movie_list_frag);
            listFrag.notifyFavoriteDeleted(position);
            Bundle args = new Bundle();
            args.putInt("movieId",0);
            detailFrag.updateContent(args);
        }
    }


}




