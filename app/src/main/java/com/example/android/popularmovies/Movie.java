package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by monac on 1/22/2017.
 */

public class Movie implements  Parcelable {

    private Date mDate;
    private double mRating;
    private String mPosterLink;
    private String mTitle;
    private String mOverview;

    private static final String BASE_IMAGES_PATH =
            "http://image.tmdb.org/t/p/w500";

    public Movie (JSONObject object){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd") ;
        try {
            mDate = df.parse(object.getString("release_date"));
            mOverview = object.getString("overview");
            mRating = object.getDouble("vote_average");
            mTitle = object.getString("title");
            mPosterLink = object.getString("poster_path");
        } catch (ParseException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getRating() {
        return mRating;
    }

    public Date getDate() {
        return mDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterLink() {
        return BASE_IMAGES_PATH + mPosterLink;
    }

    public String getTitle() {
        return mTitle;
    }



    protected Movie(Parcel in) {
        long tmpMDate = in.readLong();
        mDate = tmpMDate != -1 ? new Date(tmpMDate) : null;
        mRating = in.readDouble();
        mPosterLink = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mDate != null ? mDate.getTime() : -1L);
        dest.writeDouble(mRating);
        dest.writeString(mPosterLink);
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
}