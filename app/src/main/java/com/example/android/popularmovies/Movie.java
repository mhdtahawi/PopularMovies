package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by monac on 1/22/2017.
 */

public class Movie implements Parcelable {


    private int mId;
    private String mDate;
    private double mRating;
    private String mPosterPath;
    private String mTitle;
    private String mOverview;

    private static final String BASE_IMAGES_PATH =
            "http://image.tmdb.org/t/p/w500";

    public Movie (int id, String date, double rating, String posterPath, String title, String Overview){
        mId = id;
        mDate = date;
        mRating = rating;
        mPosterPath = posterPath;
        mTitle = title;
        mOverview = Overview;

    }

    public Movie(JSONObject object) {


        try {
            mDate = object.getString("release_date");
            mOverview = object.getString("overview");
            mRating = object.getDouble("vote_average");
            mTitle = object.getString("title");
            mPosterPath = object.getString("poster_path");
            mId = object.getInt("id");

        }  catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public double getRating() {
        return mRating;
    }

    public String getDate() {
        return mDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterLink() {
        return BASE_IMAGES_PATH + mPosterPath;
    }
    public String getPosterPath (){return mPosterPath;}

    public String getTitle() {
        return mTitle;
    }

    public int getId() {
        return mId;
    }


    protected Movie(Parcel in) {
        mId = in.readInt();

        mDate = in.readString();
        mRating = in.readDouble();
        mPosterPath = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mDate);
        dest.writeDouble(mRating);
        dest.writeString(mPosterPath);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static ArrayList<Movie> getMoviesData(String object) throws JSONException {


        JSONObject ob = new JSONObject(object);

        JSONArray arr = (JSONArray) ob.get("results");

        ArrayList<Movie> res = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {



            res.add(new Movie(arr.getJSONObject(i)));
        }

        return res;
    }


    public static ArrayList<String> getReviews(String object) throws JSONException {
        JSONObject ob = new JSONObject(object);

        JSONArray arr = (JSONArray) ob.get("results");

        ArrayList<String> res = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {

            String review = arr.getJSONObject(i).getString("content");

            res.add(review);
        }

        return res;

    }

    public static ArrayList<String> getTrailers(String object) throws JSONException {

        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

        JSONObject ob = new JSONObject(object);


        JSONArray arr = (JSONArray) ob.get("results");

        ArrayList<String> res = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {

            JSONObject reviewObject = arr.getJSONObject(i);

            String site = reviewObject.getString("site");
            String type = reviewObject.getString("type");

            if (site.toLowerCase().equals("youtube")   && type.toLowerCase().equals("trailer")  ) {
                String link = arr.getJSONObject(i).getString("key");

                res.add(YOUTUBE_BASE_URL + link);
            }
        }

        return res;

    }
}