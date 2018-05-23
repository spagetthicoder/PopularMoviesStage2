package com.example.android.popularmoviesstage2;

import android.net.Uri;

public class ReviewData {

    private String author;
    private String content;
    private Uri reviewUri;

    public ReviewData(String author, String content, Uri reviewUri) {
        this.author = author;
        this.content = content;
        this.reviewUri = reviewUri;
    }

    protected String getAuthor() {return this.author;}

    protected String getContent() { return this.content;}
}
