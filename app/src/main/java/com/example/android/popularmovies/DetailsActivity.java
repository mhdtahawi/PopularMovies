package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>, ToggleButton.OnClickListener {


    private static final String TAG = DetailsActivity.class.getSimpleName();
    TextView mTitle, mRating, mPlot, mDate;
    ImageView mPoster;
    ToggleButton mStar;
    Movie mMovie;

    boolean mIsFavourite;
    final String IS_FAV_KEY = "favKey";

    final int TRAILER_AND_REVIEW_LOADER_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mStar = (ToggleButton) findViewById(R.id.star_button);
        mStar.setOnClickListener(this);


        if (savedInstanceState != null) {
            mIsFavourite = savedInstanceState.getBoolean(IS_FAV_KEY);
            mStar.setChecked(mIsFavourite);
        }



        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("MOVIE")) {
            mMovie = intent.getParcelableExtra("MOVIE");


        Cursor cursor = getContentResolver().query(MovieContract.buildMovieUriWithId(mMovie.getId()), null, null, null, null);

            if (cursor.getCount() > 0){
                mStar.setChecked(true);
            }



            mTitle = (TextView) findViewById(R.id.tv_movie_name);
            mTitle.setText(mMovie.getTitle());


            mRating = (TextView) findViewById(R.id.tv_movie_rating);
            mRating.setText(String.valueOf(mMovie.getRating()).toString() + "/10");

            mDate = (TextView) findViewById(R.id.tv_movie_year);
            mDate.setText(mMovie.getDate());

            mPlot = (TextView) findViewById(R.id.tv_movie_overview);
            mPlot.setText(mMovie.getOverview());

            mPoster = (ImageView) findViewById(R.id.iv_poster);
            Picasso.with(mPoster.getContext()).load(mMovie.getPosterLink()).into(mPoster);
        }
        Log.d(TAG, "INIT LOADER");
        getSupportLoaderManager().initLoader(TRAILER_AND_REVIEW_LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        outState.putBoolean(IS_FAV_KEY, mStar.isChecked());

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            @Override
            protected void onStartLoading() {
                Log.d("TAG", "START LOADER FOR TRAILER");
                forceLoad();
                super.onStartLoading();

            }

            @Override
            public String[] loadInBackground() {

                if (!NetworkUtils.isOnline(getApplicationContext()))
                    return null;

                String[] res = new String[2];

                URL trailerURL = NetworkUtils.createTrailersQueryURL(mMovie.getId());
                URL reviewsURL = NetworkUtils.createReviewsQueryURL(mMovie.getId());


                try {
                    res[0] = NetworkUtils.getResponseFromHttpUrl(trailerURL);
                    res[1] = NetworkUtils.getResponseFromHttpUrl(reviewsURL);
                } catch (IOException e) {
                    Log.d(TAG, "Failed to load reviwes: ", e);
                }

                Log.d("TAG", "START LOADING FOR TRAILER");
                return res;

            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {

        try {
            ArrayList<String> trailers = Movie.getTrailers(data[0]);
            ArrayList<String> reviews = Movie.getReviews(data[1]);

            LinearLayout parent = (LinearLayout) findViewById(R.id.ll_trailers);


            if (!trailers.isEmpty())

             {
                 parent.setVisibility(View.VISIBLE);
                int i = 1;
                for (String trailer : trailers) {


                    View view = getLayoutInflater().from(parent.getContext()).inflate(R.layout.trailer_view, parent, false);

                    view.setVisibility(View.VISIBLE);
                    TextView tv = (TextView) view.findViewById(R.id.tv_movie_trailer);
                    tv.setTag(trailer);
                    tv.setText("trailer " + i++);

                    view.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {
                                                  String link = (String) v.getTag();
                                                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                                  Intent chooser = Intent.createChooser(intent, "Choose App");
                                                  if (intent.resolveActivity(getPackageManager()) != null) {
                                                      startActivity(chooser);
                                                  }

                                              }
                                          }

                    );

                    parent.addView(view);
                }

            }


            parent = (LinearLayout) findViewById(R.id.ll_reviews);

            if (! reviews.isEmpty())
             {
                 parent.setVisibility(View.VISIBLE);
                for (String review : reviews) {
                    TextView view = new TextView(parent.getContext());
                    view.setText(review);
                    parent.addView(view);

                }
            }



        } catch (JSONException e) {
            Log.d(TAG, "Invalid Json response", e);
        }


    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.star_button:
                if (mStar.isChecked()) {
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_ID, mMovie.getId());
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPosterPath());
                    cv.put(MovieContract.MovieEntry.COLUMN_PLOT, mMovie.getOverview());
                    cv.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
                    cv.put(MovieContract.MovieEntry.COLUMN_DATE, mMovie.getDate());
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv );
                    Toast.makeText(this, "Added to favourite.", Toast.LENGTH_SHORT).show();
                }
                else {
                    getContentResolver().delete(MovieContract.buildMovieUriWithId(mMovie.getId()), null, null);
                    Toast.makeText(this, "Deleted from favourite.", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }
}
