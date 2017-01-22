package com.example.android.popularmovies;

import android.net.Uri;

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

    public enum QueryCriteria {POPULARITY, RATING};

    private static final String POPULAR_BASE_LINK =
            "https://api.themoviedb.org/3/movie/popular";

    private static final String RATING_BASE_LINK =
            "https://api.themoviedb.org/3/movie/top_rated";

    private static final String BASE_IMAGES_PATH =
            "http://image.tmdb.org/t/p/w500";


    public static URL createQueryURL(QueryCriteria criteria){

        String base = criteria == QueryCriteria.POPULARITY ? POPULAR_BASE_LINK : RATING_BASE_LINK;

        Uri uri = Uri.parse(base)
                                .buildUpon()
                                .appendQueryParameter("api_key", "f9922445d2b8a62c6201a3311c0d8f67")
                                .appendQueryParameter("language", "en-us")
                                .build();

        URL res = null;

        try {
            res  = new URL(uri.toString());
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


        } finally {
            connection.disconnect();
        }


    }

    public static ArrayList<Movie> getMoviesData(String object) throws JSONException {

        JSONArray  arr = (JSONArray) new JSONObject(object).get("results");

        ArrayList<Movie> res = new ArrayList<>();

        for (int i = 0; i <arr.length() ; i++) {

            arr.getJSONObject(i);

            res.add(new Movie(arr.getJSONObject(i)));
        }

        return res;
        }


    }

