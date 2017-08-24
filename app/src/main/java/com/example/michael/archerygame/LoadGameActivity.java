package com.example.michael.archerygame;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.michael.archerygame.data.GameContract.GameEntry;

import java.util.ArrayList;

public class LoadGameActivity extends AppCompatActivity {

    private ArrayList<Long> gameIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getGameDates());
        ListView listView = (ListView) findViewById(R.id.gameListView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gameIntent = new Intent(LoadGameActivity.this, GameActivity.class);
                gameIntent.putExtra("GAME_ID", gameIdList.get(position));
                startActivity(gameIntent);
            }
        });
    }

    private ArrayList<String> getGameDates() {
        String[] projection = {
                GameEntry._ID,
                GameEntry.COLUMN_GAME_DATE,
                GameEntry.COLUMN_GAME_DATE_NR
        };

        ArrayList<String> gameList = new ArrayList<>();

        try (Cursor cursor = getContentResolver().query(GameEntry.CONTENT_URI, projection, null, null, GameEntry.COLUMN_GAME_DATE + " DESC, " +GameEntry.COLUMN_GAME_DATE_NR + " DESC")) {
            while (cursor != null && cursor.moveToNext()) {
                String gameDate = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_DATE));
                String gameDateNr = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_DATE_NR));
                Long gameId = cursor.getLong(cursor.getColumnIndex(GameEntry._ID));
                gameList.add(gameDate + ": " + gameDateNr);
                gameIdList.add(gameId);
            }
        }
        return gameList;
    }

}
