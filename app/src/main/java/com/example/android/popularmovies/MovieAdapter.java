package com.example.android.popularmovies;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by monac on 1/21/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviePoster> {

    public interface PosterClickListener {
        void onPosterClick(Movie movie);
    }

    private PosterClickListener mListener;
    private ArrayList<Movie> mMovies;

    MovieAdapter(ArrayList<Movie> movies, PosterClickListener listener) {
        mMovies = movies;
        mListener = listener;
    }


    @Override
    public MoviePoster onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, false);
        MoviePoster poster = new MoviePoster(v);
        return poster;
    }

    @Override
    public void onBindViewHolder(MoviePoster holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }





    class MoviePoster extends RecyclerView.ViewHolder
     implements View.OnClickListener{

        private Movie movie;

        ImageView mPoster;

        public MoviePoster(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(this);
        }


        public void bind(Movie movie) {
            Picasso.with(mPoster.getContext()).load(movie.getPosterLink()).into(mPoster);
            this.movie = movie;
        }

        @Override
        public void onClick(View v) {


            Log.d("TAG", "onClick in view holder");
           mListener.onPosterClick(movie);



        }
    }
}
