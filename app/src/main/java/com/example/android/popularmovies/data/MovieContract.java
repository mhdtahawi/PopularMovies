package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by monac on 2/22/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static class MovieEntry implements BaseColumns{ // not sure if I need the _id from BaseColumns, since I will use moive ID from the site

        public static final Uri CONTENT_URI = MovieContract.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "Movies";


        public static String COLUMN_ID = "MOVIE_ID"; // maybe I should just call it _id and forget about BaseColumns
        public static String COLUMN_TITLE = "TITLE";
        public static String COLUMN_POSTER = "POSTER";
        public static String COLUMN_DATE = "DATE";
        public static String COLUMN_RATING = "RATING";
        public static String COLUMN_PLOT = "PLOT";

    }

    public static Uri buildMovieUriWithId(int id) {
        return MovieEntry.CONTENT_URI.buildUpon()
                .appendPath(Integer.toString(id))
                .build();
    }

}
