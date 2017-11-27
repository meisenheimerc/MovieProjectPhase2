package com.example.android.movieprojectphase2.data;

import android.net.Uri;
import android.provider.BaseColumns;



public class MovieContract {
    public static final String AUTHORITY = "com.example.android.movieprojectphase2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";



    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_IMAGE = "movieImage";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }

}
