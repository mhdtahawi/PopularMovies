package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by monac on 2/23/2017.
 */

public class MovieProvider extends ContentProvider {


    private static final int CODE_MOVIE = 100;
    private static final int CODE_MOVIE_WITH_ID = 101;

    private MovieDbHelper mDbHelopr;

    private static UriMatcher sMatcher = createMatcher();

    private static UriMatcher createMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, CODE_MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/*", CODE_MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {

        mDbHelopr = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sMatcher.match(uri)) {

            case CODE_MOVIE:

                cursor = mDbHelopr.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_MOVIE_WITH_ID:

                String id = uri.getLastPathSegment();

                cursor = mDbHelopr.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDbHelopr.getWritableDatabase();
        long id = -1;

        switch (sMatcher.match(uri)) {

            case CODE_MOVIE:
                try {
                    id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, values);
                } catch (SQLException e){
                    Log.d("TAG", "Failed to insert ", e);
                }
//                if (id == -1)
//                    throw new SQLException("Failed to insert row into database");
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI "  + uri);

        }

        Uri returnUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelopr.getWritableDatabase();

        int rowsDeleted = 0;

        switch (sMatcher.match(uri)) {

            case CODE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_ID + "=?", new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI");

        }

        if (rowsDeleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
