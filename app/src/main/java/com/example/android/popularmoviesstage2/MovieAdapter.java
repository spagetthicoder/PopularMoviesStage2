package com.example.android.popularmoviesstage2;

/**
 * Created by hobbit2 on 1.5.2018 г..
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hobbit2 on 2.4.2018 г..
 */

public class MovieAdapter extends BaseAdapter {

    private final ArrayList<Movie> finalMoviePosters = new ArrayList<>();
    private final String TAG = MovieAdapter.class.getSimpleName();
    private final Context mContext;
    private final float density;


    public MovieAdapter(Context c) {
        mContext = c;
        density = mContext.getResources().getDisplayMetrics().density;
    }

    public int getCount() { return finalMoviePosters.size();}

    public long getItemId(int position) {
        return finalMoviePosters.get(position).getMovieId();
    }

    public Object getItem(int position) {
        return finalMoviePosters.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0,0,0,0);
            imageView.setBackgroundColor(Color.rgb(0,0,0));
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso pic = Picasso.with(mContext);

        pic.load(finalMoviePosters.get(position).getMoviePoster())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .into(imageView);

        return imageView;
    }

    public void add(Movie res) {
        finalMoviePosters.add(res);
    }

    public void remove(int position) {

        Log.e(TAG, "Remove position:"+position+" finalMoviePosters.size:"+finalMoviePosters.size());
        finalMoviePosters.remove(position);
    }

    public void clearData() {
        finalMoviePosters.clear();
        notifyDataSetChanged();
    }

}
