package com.example.android.popularmoviesstage2;

public class MovieDetail extends Movie {

    public final String movieTitle;
    public final String movieOverview;
    public final String releaseDate;
    public final Double vote_average;
    public final String TAG = MovieDetail.class.getSimpleName();
    private String poster;

    public MovieDetail(int movieId, String poster, String moviePoster,String movieTitle, String movieOverview, String releaseDate, Double vote_average) {
        super(moviePoster, movieId);

        this.movieTitle = movieTitle;
        this.poster = poster;
        this.movieOverview = movieOverview;
        this.releaseDate = releaseDate;
        this.vote_average = vote_average;
    }

    public String getMovieTitle() { return this.movieTitle;}

    public String getMovieOverview() { return this.movieOverview;}

    public String getMoveReleaseYear() { return releaseDate;}

    public Double getVoteAverage() { return this.vote_average;}

}

