package com.example.android.movieprojectphase2.data;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mTitle;
    private String mPosterPath;
    private String mOverview;
    private double mVoteAverage;
    private String mReleaseDate;
    private int mMovieId;

    public Movie(String title, String posterPath, String overview, double voteAverage, String releaseDate, int movieId) {
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        mMovieId = movieId;
    }

    public Movie(String title, String posterPath) {
        mTitle = title;
        mPosterPath = posterPath;
    }


    public Movie(int movieId) {
        mMovieId = movieId;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
        mMovieId = in.readInt();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getMovieId() {
        return mMovieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeInt(mMovieId);

    }
}
