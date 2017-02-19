package com.example.android.popularmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by monac on 2/19/2017.
 */

public class TrailerView extends LinearLayout {

    TextView trailerTextView;

    public TrailerView(Context context) {
        super(context);

        trailerTextView = (TextView) findViewById(R.id.tv_movie_trailer);


    }


    public void setText(String text) {
        trailerTextView.setText(text);
    }
}
