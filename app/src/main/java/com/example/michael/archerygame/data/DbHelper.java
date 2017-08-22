package com.example.michael.archerygame.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;
import com.example.michael.archerygame.data.GameContract.GameEntry;

class DbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "game.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 3;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PLAYERS_TABLE =  "CREATE TABLE " + PlayerEntry.TABLE_NAME + " ("
                + PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PlayerEntry.COLUMN_GAME_ID + " INTEGER NOT NULL, "
                + PlayerEntry.COLUMN_PLAYER_NAME + " TEXT NOT NULL, "
                + PlayerEntry.COLUMN_PLAYER_TEAM + " INTEGER NOT NULL, "
                + PlayerEntry.COLUMN_PLAYER_POINTS + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PLAYERS_TABLE);

        String SQL_CREATE_GAMES_TABLE = "CREATE TABLE " + GameEntry.TABLE_NAME + " ("
                + GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GameEntry.COLUMN_GAME_NAME_TEAM_A + " DEFAULT \"Team A\", "
                + GameEntry.COLUMN_GAME_NAME_TEAM_B + " DEFAULT \"Team B\", "
                + GameEntry.COLUMN_GAME_DATE + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_DATE_NR + " INTEGER NOT NULL DEFAULT 1);";

        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL("DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        onCreate(db);
    }
}
