package com.example.michael.archerygame.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;
import com.example.michael.archerygame.data.GameContract.GameEntry;

class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game.db";
    private static final int DATABASE_VERSION = 5;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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
                + GameEntry.COLUMN_GAME_NAME_TEAM_A + " TEXT DEFAULT \"Team A\", "
                + GameEntry.COLUMN_GAME_NAME_TEAM_B + " TEXT DEFAULT \"Team B\", "
                + GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_A + " INTEGER DEFAULT 0, "
                + GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_B + " INTEGER DEFAULT 0, "
                + GameEntry.COLUMN_GAME_TEAM_TURN + " INTEGER DEFAULT 0, "
                + GameEntry.COLUMN_GAME_DATE + " TEXT NOT NULL, "
                + GameEntry.COLUMN_GAME_DATE_NR + " INTEGER NOT NULL DEFAULT 1);";

        db.execSQL(SQL_CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        onCreate(db);
    }
}