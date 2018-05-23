package com.example.android.popularmoviesstage2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.database.MovieContract;
import com.example.android.popularmoviesstage2.database.MovieProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailFragment extends android.app.Fragment {

    private final String TAG = MovieDetailFragment.class.getSimpleName();
    private String sortOrder;
    private View rootView;
    private int position;

    private int movieId;
    private MovieDetail detailsData;
    private TextView movieTitle;
    private ImageView thumbImageView;
    private TextView releaseYear;
    private TextView rating;
    private TextView overview;
    private ImageView favsIcon;
    private LinearLayout trailersListView;
    private LinearLayout reviewsListView;
    private JSONObject trailerObj;
    private JSONObject reviewObj;
    private List<TrailerData> trailerData;
    private List<ReviewData> reviewData;
    private boolean isFavorite;
    private ScrollView detailScrollView;
    private TextView tv_SelectMovie;
    private OnMovieClickListener mCallback;

    final String MOVIEDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    public MovieDetailFragment() {

    }

    public static MovieDetailFragment newInstance(int movieId, String sortOrder) {
        MovieDetailFragment f = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putInt("movieId", movieId);
        args.putString("sortOrder", sortOrder);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {

        super.onActivityCreated(savedInstance);
        rootView = this.getView();
        // getFragmentArguments();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMovieClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setupMovieDetailsView();
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (getActivity().getClass().getSimpleName().compareTo("MainActivity") == 0) {

            tv_SelectMovie.setVisibility(View.VISIBLE);
            detailScrollView.setVisibility(View.INVISIBLE);
        }
        Log.e(TAG,"onResume calling activity name:"+getActivity().getClass().getSimpleName());
    }

   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }*/

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateContent(Bundle args) {
        movieId = args.getInt("movieId");
        if (movieId == 0) {
            tv_SelectMovie.setVisibility(View.VISIBLE);
            detailScrollView.setVisibility(View.INVISIBLE);
            return;
        }
        sortOrder = args.getString("sortOrder");
        position = args.getInt("position");

        trailersListView.removeAllViews();
        reviewsListView.removeAllViews();
        tv_SelectMovie.setVisibility(View.GONE);
        detailScrollView.setVisibility(View.VISIBLE);

        if(sortOrder.equals("favorites")){
            fetchFavoriteMovieData(movieId);
        }else
            fetchMovieData(movieId);
    }

    private void setupMovieDetailsView() {
        movieTitle = (TextView)rootView.findViewById(R.id.movie_title);
        thumbImageView = (ImageView)rootView.findViewById(R.id.thumb_imageView);
        releaseYear = (TextView)rootView.findViewById(R.id.release_date);
        rating = (TextView)rootView.findViewById(R.id.rating);
        overview = (TextView)rootView.findViewById(R.id.overview);
        favsIcon = (ImageView)rootView.findViewById(R.id.favorites_icon);
        trailersListView = (LinearLayout)rootView.findViewById(R.id.trailer_list);
        reviewsListView = (LinearLayout)rootView.findViewById(R.id.reviews_list);
        detailScrollView = (ScrollView)rootView.findViewById(R.id.sv_movieDetail);
        tv_SelectMovie = (TextView)rootView.findViewById(R.id.tv_selectMovieDetail);
    }

    private void populateDetailsViewData(final MovieDetail detailData) {

        Picasso pic = Picasso.with(getActivity().getApplicationContext());
        pic.load(detailData.getMoviePoster())
                .error(R.drawable.no_image)
                .into(thumbImageView);
        movieTitle.setText(detailData.getMovieTitle());
        releaseYear.setText(detailData.getMoveReleaseYear());
        overview.setText(detailData.getMovieOverview());
        overview.setEllipsize(TextUtils.TruncateAt.END);
        overview.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        rating.setText(detailData.getVoteAverage()+"/10");

        //check if this movie is inserted into favorites
        Uri movieUri = Uri.parse(MovieProvider.CONTENT_URI+"/"+detailData.getMovieId());
        String[] projection = new String[]{MovieContract.MovieEntry._ID};
        Cursor cursor = getActivity().getContentResolver().query(movieUri,projection,null, null, null);
        if (cursor.getCount() == 0) {
            favsIcon.setImageResource(R.drawable.baseline_star_border_black_18dp);
            isFavorite = false;
        }
        else {
            favsIcon.setImageResource(R.drawable.baseline_star_black_18dp);
            isFavorite = true;
        }
        favsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFavorite) {
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry._ID, detailData.getMovieId());
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, detailData.getMovieTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, detailData.getMovieOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, detailData.getVoteAverage());
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, detailData.getMoveReleaseYear());
                    values.put(MovieContract.MovieEntry.COLUMN_POSTER, detailData.getMoviePoster());
                    if (trailerObj.length()>0)
                        values.put(MovieContract.MovieEntry.COLUMN_TRAILERS, trailerObj.toString());
                    else
                        values.put(MovieContract.MovieEntry.COLUMN_TRAILERS, "");
                    if (reviewObj.length() > 0)
                        values.put(MovieContract.MovieEntry.COLUMN_REVIEWS, reviewObj.toString());
                    else
                        values.put(MovieContract.MovieEntry.COLUMN_REVIEWS, "");
                    Uri uri = getActivity().getContentResolver().insert(MovieProvider.CONTENT_URI, values);

                    if (uri != null) {
                        favsIcon.setImageResource(R.drawable.baseline_star_black_18dp);
                        isFavorite = true;
                    }
                }
                else {
                    favsIcon.setImageResource(R.drawable.baseline_star_border_black_18dp);
                    Uri deleteUri = Uri.parse(MovieProvider.CONTENT_URI+"/"+detailData.getMovieId());
                    int count = getActivity().getContentResolver().delete(deleteUri, null, null);
                    isFavorite = false;
                    mCallback.onFavoriteDeleted(movieId, position);
                }
            }
        });
    }

    private void populateTrailersView(final List<TrailerData> trailerData) {
        if (trailerData.size()==0) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.no_item, null);
            TextView noItem = (TextView) view.findViewById(R.id.no_item_text);
            noItem.setText("No Trailers");
            trailersListView.addView(view);
            return;
        }

        for (final TrailerData trailer : trailerData) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.trailer_list_item,null);
            TextView trailerName = (TextView) view.findViewById(R.id.trailer_list_item_title);
            trailerName.setText(trailer.getTrailerName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "Trailer URI:"+trailer.getTrailerUri().toString());
                    startActivity(new Intent(Intent.ACTION_VIEW, trailer.getTrailerUri()));
                }
            });
            trailersListView.addView(view);
        }
        return;
    }

    private void populateReviewsView(final List<ReviewData> reviewsData) {

        if (reviewsData.size()==0) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.no_item, null);
            TextView noItem = (TextView) view.findViewById(R.id.no_item_text);
            noItem.setText("No User Reviews");
            reviewsListView.addView(view);
            return;
        }
        for (final ReviewData review : reviewsData) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.review_item, null);
            TextView author = (TextView) view.findViewById(R.id.review_author);
            author.setText("A review by "+review.getAuthor());
            TextView content = (TextView) view.findViewById(R.id.review_content);
            content.setText(review.getContent());
            reviewsListView.addView(view);
        }
        return;
    }

    private void fetchMovieData(int movieId) {
        FetchDetailsMovieTask detailTask = new FetchDetailsMovieTask();
        detailTask.execute(movieId);

        FetchTrailersTask trailersTask = new FetchTrailersTask();
        trailersTask.execute(movieId);

        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        reviewsTask.execute(movieId);
    }

    private void fetchFavoriteMovieData(int movieId) {
        String[] projection = new String[]{MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_POSTER,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_REVIEWS,
                MovieContract.MovieEntry.COLUMN_TRAILERS
        };
        Uri uri = Uri.parse(MovieProvider.CONTENT_URI+"/"+movieId);
        final Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        detailsData = new MovieDetail(movieId,
                cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)),
                cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE))
        );

        try {
            JSONObject trailerJObj = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TRAILERS)));
            JSONObject reviewJObj  = new JSONObject(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_REVIEWS)));

            parseTrailerObj(trailerJObj);
            populateTrailersView(trailerData);

            JSONArray reviewResultArray = reviewJObj.getJSONArray("results");
            reviewData = new ArrayList<ReviewData>();
            for (int i=0; i < reviewResultArray.length(); i++) {
                JSONObject reviewObj = reviewResultArray.getJSONObject(i);
                String author = reviewObj.getString("author");
                String content = reviewObj.getString("content");
                Uri reviewUri = Uri.parse(reviewObj.getString("url"));
                ReviewData reviewItem = new ReviewData(author, content, reviewUri);
                reviewData.add(reviewItem);
            }
            populateReviewsView(reviewData);

        }catch(JSONException e) {
            Log.e(TAG,"fetchFavoriteMovieData e:"+e);
        }

        populateDetailsViewData(detailsData);
        //populateTrailersView(trailers);
        //populateReviewsView(reviews);
    }

    private void parseTrailerObj(JSONObject jObj) {
        trailerData = new ArrayList<TrailerData>();
        try {
            JSONArray trailersResultArray = jObj.getJSONArray("results");

            for (int i = 0; i < trailersResultArray.length(); i++) {
                JSONObject trailerDetails = trailersResultArray.getJSONObject((i));
                Uri trailerUri = Uri.parse(new String("http://www.youtube.com/watch?v="+trailerDetails.getString("key")));
                TrailerData trailerEntry = new TrailerData(trailerUri, trailerDetails.getString("name"));
                trailerData.add(trailerEntry);
            }

        } catch (JSONException e) {
            Log.e(TAG,"FetchTrailersTask Exception:",e);
        }
        return;
    }

    private class FetchDetailsMovieTask extends AsyncTask<Integer, Void, JSONObject> {
        private final String TAG = FetchDetailsMovieTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/"+params[0]);
            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            if(jObj != null) {

                try {
                    //poster_path, title, overview, release_year, run time, ratings
                    detailsData = new MovieDetail(movieId, jObj.getString("poster_path"),
                            jObj.getString("poster_path"),
                            jObj.getString("title"),
                            jObj.getString("overview"),
                            jObj.getString("release_date"),
                            jObj.getDouble("vote_average"));
                    populateDetailsViewData(detailsData);
                } catch(JSONException e) {
                    Log.e(TAG,"onPostExecute e:"+e);
                }
            }
        }
    }

    private class FetchTrailersTask extends AsyncTask<Integer, Void, JSONObject> {
        private final String TAG = FetchTrailersTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0]+"/videos");
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                trailerObj = jObj;
                parseTrailerObj(jObj);

                populateTrailersView(trailerData);
            }
        }
    }

    private class FetchReviewsTask extends AsyncTask<Integer, Void, JSONObject> {
        private final String TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected JSONObject doInBackground(Integer... params) {
            JSONObject jObj = JSONLoader.load("/movie/" + params[0] + "/reviews");
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            reviewObj = jObj;

            if (jObj != null) {
                reviewData = new ArrayList<ReviewData>();

                try {
                    JSONArray reviewResultArray = jObj.getJSONArray("results");
                    for (int i=0; i < reviewResultArray.length(); i++) {
                        JSONObject reviewObj = reviewResultArray.getJSONObject(i);
                        String author = reviewObj.getString("author");
                        String content = reviewObj.getString("content");
                        Uri reviewUri = Uri.parse(reviewObj.getString("url"));
                        ReviewData reviewItem  = new ReviewData(author, content, reviewUri);
                        reviewData.add(reviewItem);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "FetchReviewsTask e:"+e);
                }
                populateReviewsView(reviewData);
            }
        }
    }



}
