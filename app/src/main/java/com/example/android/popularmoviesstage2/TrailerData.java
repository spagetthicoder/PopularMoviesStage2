package com.example.android.popularmoviesstage2;

import android.net.Uri;

public class TrailerData {
    private Uri trailerUri;
    private String trailerName;

    public TrailerData(Uri trailerUri, String trailerName) {
        this.trailerUri = trailerUri;
        this.trailerName = trailerName;
    }

    public Uri getTrailerUri() { return trailerUri;}

    public String getTrailerName() { return trailerName;}
}

