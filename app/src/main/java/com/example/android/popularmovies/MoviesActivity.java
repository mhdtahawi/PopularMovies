package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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

public class MoviesActivity extends AppCompatActivity implements MovieAdapter.PosterClickListener {

    private QueryCriteria mSortingOrder = QueryCriteria.POPULARITY;

    private RecyclerView mMovieGrid;
    private GridLayoutManager mGridManager;
    private MovieAdapter mMovieAdapter;
    private final int NUM_OF_COLS = 2;
    private LinearLayout mLoadingSection;
    private TextView mLoadTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mLoadingSection = (LinearLayout) findViewById(R.id.ll_loading);
        mLoadTextView = (TextView) findViewById(R.id.tv_loading_message);

        mMovieGrid = (RecyclerView) findViewById(R.id.rv_grid);
        mGridManager = new GridLayoutManager(this, NUM_OF_COLS);
        mMovieGrid.setLayoutManager(mGridManager);

        fetchData();

    }


    private void fetchData(){
        mLoadTextView.setText(R.string.loading_message);
        showLoadingSection();
        new movieQueryTask().execute(NetworkUtils.createQueryURL(mSortingOrder));
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


    public class movieQueryTask extends AsyncTask<URL, Void, String> {

        boolean isConnected;

        public boolean isOnline() {

            return true;
//            Runtime runtime = Runtime.getRuntime();
//            try {
//
//                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//                int exitValue = ipProcess.waitFor();
//                Log.d("TAG", "VALUE IS " + String.valueOf(exitValue));
//                return (exitValue == 0);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return false;
        }

        @Override
        protected void onPreExecute() {
            showLoadingSection();
            isConnected = isOnline();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {

            String res = null;

            if (!isConnected)
                return res;
            try {
                res = NetworkUtils.getResponseFromHttpUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!isConnected) {
                mLoadTextView.setText(R.string.error_message);
                return;
            }


            try {

                mMovieAdapter = new MovieAdapter(NetworkUtils.getMoviesData(s), MoviesActivity.this);

                mMovieGrid.setAdapter(mMovieAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            showGridSection();
            super.onPostExecute(s);
        }

    }
}
