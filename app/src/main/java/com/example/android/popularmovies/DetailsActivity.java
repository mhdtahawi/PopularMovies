package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {


    TextView mTitle, mRating, mPlot, mDate;
    ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();


        Movie movie =  intent.getParcelableExtra("MOVIE");

        mTitle = (TextView) findViewById(R.id.tv_movie_name);
        mTitle.setText(movie.getTitle());


        mRating = (TextView) findViewById(R.id.tv_movie_rating);
        mRating.setText(String.valueOf(movie.getRating()).toString() + "/10");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mDate = (TextView) findViewById(R.id.tv_movie_year);
        mDate.setText(df.format(movie.getDate()));

        mPlot = (TextView) findViewById(R.id.tv_movie_overview);
        mPlot.setText(movie.getOverview());

        mPoster = (ImageView) findViewById(R.id.iv_poster);
        Picasso.with(mPoster.getContext()).load(movie.getPosterLink()).into(mPoster);




    }
}
