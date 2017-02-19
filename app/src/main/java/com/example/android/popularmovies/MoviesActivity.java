package com.example.android.popularmovies;

import android.content.Intent;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.NetworkUtils.*;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.PosterClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private QueryCriteria mSortingOrder = QueryCriteria.POPULARITY;

    private RecyclerView mMovieGrid;
    private GridLayoutManager mGridManager;
    private MovieAdapter mMovieAdapter;
    private LinearLayout mLoadingSection;
    private TextView mLoadTextView;

    private final int MOVIE_LOADER_ID = 1;
    private final String MOVIE_LOADER_KEY_EXTRA = "LOAD_MOVIES";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mLoadingSection = (LinearLayout) findViewById(R.id.ll_loading);
        mLoadTextView = (TextView) findViewById(R.id.tv_loading_message);


        mMovieGrid = (RecyclerView) findViewById(R.id.rv_grid);
        mGridManager = new GridLayoutManager(this, getResources().getInteger(R.integer.gallery_columns));
        mMovieGrid.setLayoutManager(mGridManager);

        fetchData();

    }


    private void fetchData() {
        mLoadTextView.setText(R.string.loading_message);
        showLoadingSection();


        Bundle args = new Bundle();
        args.putString(MOVIE_LOADER_KEY_EXTRA, NetworkUtils.createMovieQueryURL(mSortingOrder).toString());

        if (getSupportLoaderManager().getLoader(MOVIE_LOADER_ID) == null)
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, args, this);
        else
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, args, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.refresh:
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
                fetchData();
                break;

            case R.id.by_popularity:
                mSortingOrder = QueryCriteria.POPULARITY;
                fetchData();
                break;

            case R.id.by_rating:
                mSortingOrder = QueryCriteria.RATING;
                fetchData();
                break;
        }


        return true;
    }

    public void showLoadingSection() {
        mMovieGrid.setVisibility(View.GONE);
        mLoadingSection.setVisibility(View.VISIBLE);
    }

    public void showGridSection() {
        mLoadingSection.setVisibility(View.GONE);
        mMovieGrid.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPosterClick(Movie movie) {

        Intent intent = new Intent(this, DetailsActivity.class);

        intent.putExtra("MOVIE", movie);


        Log.d("TAG", "starting another activity");
        startActivity(intent);


    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String res;

            @Override
            public String loadInBackground() {

                if (!NetworkUtils.isOnline(getApplicationContext()))
                    return res;
                try {
                    URL url = new URL(args.getString(MOVIE_LOADER_KEY_EXTRA));
                    res = NetworkUtils.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return res;

            }

            @Override
            protected void onStartLoading() {

                if (args == null) return;
                res = null;

                showLoadingSection();


                if (res != null)
                    deliverResult(res);
                else
                    forceLoad();

            }

            @Override
            public void deliverResult(String data) {
                res = data;
                super.deliverResult(data);
            }




        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (!NetworkUtils.isOnline(getApplicationContext())) {
            mLoadTextView.setText(R.string.error_message);
            return;
        }

        try {

            if (mMovieAdapter != null) {
                mMovieAdapter.changeDate(Movie.getMoviesData(data));
            } else {
                mMovieAdapter = new MovieAdapter(Movie.getMoviesData(data), MoviesActivity.this);
                mMovieGrid.setAdapter(mMovieAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showGridSection();


    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


//    public class movieQueryTask extends AsyncTask<URL, Void, String> {
//
//        boolean isConnected;
//
//        public boolean isOnline() {
//            ConnectivityManager cm =
//                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            return netInfo != null && netInfo.isConnectedOrConnecting();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            showLoadingSection();
//            isConnected = isOnline();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(URL... params) {
//
//            String res = null;
//
//            if (!isConnected)
//                return res;
//            try {
//                res = NetworkUtils.getResponseFromHttpUrl(params[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//
//    }
}
