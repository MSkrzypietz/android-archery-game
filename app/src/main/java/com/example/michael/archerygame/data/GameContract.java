package com.example.michael.archerygame.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Michael on 09.08.2017.
 */

public class GameContract {

    public static final String CONTENT_AUTHORITY = "com.example.michael.archerygame";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GAMES = "games";

    private GameContract() {
    }

    public static class GameEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GAMES);
        public final static String TABLE_NAME = "games";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAMES;

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GAME_NAME_TEAM_A = "gameNameTeamA";
        public final static String COLUMN_GAME_NAME_TEAM_B = "gameNameTeamB";
        public final static String COLUMN_GAME_DATE = "gameDate";
        public final static String COLUMN_GAME_DATE_NR = "gameDateNr";

    }
}