package com.example.michael.archerygame;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michael.archerygame.data.GameContract.GameEntry;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.michael.archerygame.R.id.teamName;

public class CreateGameActivity extends AppCompatActivity {

    //TODO: Refactor to insert data only when finished with both teams

    private long gameId;
    private String nameOfTeamA;
    private String nameOfTeamB;
    private String playerName;
    private ArrayList<String> membersOfTeamA = new ArrayList<>();
    private ArrayList<String> membersOfTeamB = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter;
    public boolean hasToCreateAnotherTeam = true;
    EditText teamNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Button addButton = (Button) findViewById(R.id.gameCreationAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText playerNameEditText = (EditText) findViewById(R.id.playerEditText);
                playerName = playerNameEditText.getText().toString().trim();
                if (hasToCreateAnotherTeam) membersOfTeamA.add(playerName);
                else membersOfTeamB.add(playerName);
                itemsAdapter.notifyDataSetChanged();
                playerNameEditText.setText("");
                /*
                // TODO: rename func to createPlayerTableEntries AND create all entries together in the end
                int teamValue = hasToCreateAnotherTeam ? PlayerEntry.TEAM_A : PlayerEntry.TEAM_B;
                createPlayerTableEntry(teamValue);
                */
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTeamName();
                // TODO: no need of updateGame anymore, simply use insertGame with the global vars nameOfTeamA/B
                if (hasToCreateAnotherTeam) {
                    changeHeader();
                    updateGame(GameEntry.COLUMN_GAME_NAME_TEAM_A);
                    hasToCreateAnotherTeam = false;
                    resetActivityToCreateAnotherTeam();
                } else {
                    insertGame();
                    updateGame(GameEntry.COLUMN_GAME_NAME_TEAM_B);
                    Intent gameIntent = new Intent(CreateGameActivity.this, GameActivity.class);
                    gameIntent.putExtra("GAME_ID", gameId);
                    startActivity(gameIntent);
                }
            }
        });

        if (hasToCreateAnotherTeam) itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, membersOfTeamA);
        else itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, membersOfTeamA);

        ListView listView = (ListView) findViewById(R.id.playerListView);
        listView.setAdapter(itemsAdapter);

        teamNameEditText = (EditText) findViewById(teamName);
    }

    private void changeHeader() {
        TextView header = (TextView) findViewById(R.id.createGameTeamView);
        header.setText(R.string.secondTeam);

        EditText addPlayer = (EditText) findViewById(R.id.playerEditText);
        addPlayer.setText("");
    }

    private void createPlayerTableEntry(int teamValue) {
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, playerName);
        values.put(PlayerEntry.COLUMN_PLAYER_TEAM, teamValue);
        values.put(PlayerEntry.COLUMN_GAME_ID, gameId);

        long playerId = ContentUris.parseId(getContentResolver().insert(PlayerEntry.CONTENT_URI, values));

        Log.v("CreateGameActivity", "New row ID = " + playerId);
    }

    private void setTeamName() {
        if (hasToCreateAnotherTeam) {
            nameOfTeamA = getTeamNameFromView();
            if (nameOfTeamA.length() == 0) nameOfTeamA = "Team A";
        } else {
            nameOfTeamB = getTeamNameFromView();
            if (nameOfTeamB.length() == 0) nameOfTeamB = "Team B";
        }
    }

    private String getTeamNameFromView() {
        return teamNameEditText.getText().toString().trim();
    }

    private void insertGame() {
        String gameDateString = getGameDate();
        String gameDateNrString = String.valueOf(getGameDateNr());

        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_GAME_NAME_TEAM_A, nameOfTeamA);
        values.put(GameEntry.COLUMN_GAME_NAME_TEAM_B, nameOfTeamB);
        values.put(GameEntry.COLUMN_GAME_DATE, gameDateString);
        values.put(GameEntry.COLUMN_GAME_DATE_NR, gameDateNrString);

        gameId = ContentUris.parseId(getContentResolver().insert(GameEntry.CONTENT_URI, values));

        Log.v("CreateGameActivity", "New row ID = " + gameId);
    }

    private void updateGame(String team) {
        ContentValues values = new ContentValues();
        values.put(team, teamName);
        getContentResolver().update(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), values, null, null);
    }

    private String getGameDate() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        return df.format(date);
    }

    private int getGameDateNr() {

        String[] projection = {
                GameEntry._ID,
                GameEntry.COLUMN_GAME_DATE,
                GameEntry.COLUMN_GAME_DATE_NR
        };

        String selection = GameEntry.COLUMN_GAME_DATE + "=?";
        String[] selectionArgs = new String[] {getGameDate()};

        try (Cursor cursor = getContentResolver().query(GameEntry.CONTENT_URI, projection, selection, selectionArgs, GameEntry.COLUMN_GAME_DATE_NR + " DESC")){
            if (cursor != null && cursor.moveToNext()) return cursor.getInt(cursor.getColumnIndex(GameEntry.COLUMN_GAME_DATE_NR)) + 1;
        }
        return 1;
    }

    private void resetActivityToCreateAnotherTeam() {
        //TODO: fix commented line
        teamNameEditText.setText("");
        //membersOfTeam.clear();
        itemsAdapter.notifyDataSetChanged();
    }

}
