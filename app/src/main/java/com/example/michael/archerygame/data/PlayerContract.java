package com.example.michael.archerygame.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PlayerContract {

    public static final String CONTENT_AUTHORITY = "com.example.michael.archerygame";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLAYERS = "players";

    private PlayerContract() {}

    public static class PlayerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAYERS);
        public final static String TABLE_NAME = "players";


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYERS;

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GAME_ID = "gameId";
        public final static String COLUMN_PLAYER_NAME = "playerName";
        public final static String COLUMN_PLAYER_TEAM = "playerTeam";
        public final static String COLUMN_PLAYER_POINTS = "playerPoints";

        public static final int TEAM_A = 0;
        public static final int TEAM_B = 1;
    }
}
