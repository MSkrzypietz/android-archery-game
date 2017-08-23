package com.example.michael.archerygame.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.michael.archerygame.data.GameContract.GameEntry;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;

/**
 * Created by Michael on 09.08.2017.
 */

public class DbProvider extends ContentProvider {
    /** Tag for the log messages */
    public static final String LOG_TAG = DbProvider.class.getSimpleName();

    private DbHelper mDbHelper;
    private static final int GAME_ID = 103;
    private static final int GAMES = 102;
    private static final int PLAYER_ID = 101;
    private static final int PLAYERS = 100;
    private static final UriMatcher sUriMatcher;


    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(GameContract.CONTENT_AUTHORITY, GameContract.PATH_GAMES, GAMES);
        sUriMatcher.addURI(GameContract.CONTENT_AUTHORITY, GameContract.PATH_GAMES + "/#",
                GAME_ID);
        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLAYERS, PLAYERS);
        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLAYERS + "/#",
                PLAYER_ID);
    }
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAME_ID:
                selection = GameEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PLAYERS:
                cursor = database.query(PlayerEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return insertGame(uri, contentValues);
            case PLAYERS:
                return insertPlayer(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertGame(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(GameEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPlayer(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PlayerEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAME_ID:
                selection = GameEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateGame(uri, contentValues, selection, selectionArgs);
            case PLAYER_ID:
                selection = PlayerEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePlayer(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateGame(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        if (values.size() == 0) return 0;

        return database.update(GameEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updatePlayer(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        if (values.size() == 0) return 0;

        return database.update(PlayerEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return database.delete(GameEntry.TABLE_NAME, selection, selectionArgs);
            /*case GAME_ID:
                selection = GameEntry._ID + " = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(GameEntry.TABLE_NAME, selection, selectionArgs);*/
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return GameEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
