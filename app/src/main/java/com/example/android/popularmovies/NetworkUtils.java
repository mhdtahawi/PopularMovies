package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by monac on 1/21/2017.
 */

public class NetworkUtils {

    static final String TAG = NetworkUtils.class.getSimpleName();

    public enum QueryCriteria {POPULARITY, RATING}




    public static URL createMovieQueryURL(QueryCriteria criteria) {
        String POPULAR_BASE_LINK =
                "https://api.themoviedb.org/3/movie/popular";

        String RATING_BASE_LINK =
                "https://api.themoviedb.org/3/movie/top_rated";

        String base = criteria == QueryCriteria.POPULARITY ? POPULAR_BASE_LINK : RATING_BASE_LINK;

        Uri uri = Uri.parse(base)
                .buildUpon()
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .appendQueryParameter("language", "en-us")
                .build();

        URL res = null;

        try {
            res = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return res;


    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {

            InputStream input = connection.getInputStream();
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            return scanner.hasNext() ? scanner.next() : null;
        }

        finally {
            connection.disconnect();
        }


    }


    public static boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static URL createTrailersQueryURL(int id) {

        String BASE_URL = "https://api.themoviedb.org/3/movie/";
        String TRAILER_PATH_SEGMENT = "videos";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(TRAILER_PATH_SEGMENT)
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .appendQueryParameter("language", "en-us")
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.d(TAG, "invalid Uri: " + uri.toString());
        }

        return url;

    }



    public static URL createReviewsQueryURL(int id) {

        String BASE_URL = "https://api.themoviedb.org/3/movie/";
        String REVIEWS_PATH_SEGMENT = "reviews";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(REVIEWS_PATH_SEGMENT)
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .appendQueryParameter("language", "en-us")
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.d(TAG, "invalid Uri: " + uri.toString());
        }

        return url;

    }


}

