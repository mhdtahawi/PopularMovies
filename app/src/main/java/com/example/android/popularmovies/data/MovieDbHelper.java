package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by monac on 2/22/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "movie.db";


    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "( " +
                MovieContract.MovieEntry._ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_ID      + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER  + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_DATE + " DOUBLE NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RATING  + " DOUBLE NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PLOT    + " TEXT NOT NULL, " +
                "UNIQUE (" +  MovieContract.MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE" +
        ");";


        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME );
        onCreate(db);

    }
}
