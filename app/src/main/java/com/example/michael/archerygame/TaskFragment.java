package com.example.michael.archerygame;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.archerygame.data.GameContract.GameEntry;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class TaskFragment extends Fragment {

    public static String nameOfTeamA;
    public static String nameOfTeamB;

    private long gameId;
    public static ArrayList<String> playerListOfTeamA = new ArrayList<>();
    public static ArrayList<String> playerListOfTeamB = new ArrayList<>();
    public static ArrayList<Integer> playerPointsOfTeamA = new ArrayList<>();
    public static ArrayList<Integer> playerPointsOfTeamB = new ArrayList<>();
    private int playerTurnCounterOfTeamA = 0;
    private int playerTurnCounterOfTeamB = 0;
    private ArrayAdapter<String> adapterOfTeamA;
    private ArrayAdapter<String> adapterOfTeamB;
    private int currentTask;
    private View rootView;
    private boolean isTeamATurn = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_task, container, false);

        initLists();
        gameId = GameActivity.gameId;
        if (gameId != -1) {
            setTeamNames();
            updateTeamNameViews();
        }

        final Button addPointsForTeamA = (Button) rootView.findViewById(R.id.addPointsForTeamA);
        addPointsForTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointsForTeamA();
            }
        });

        final Button decPointsForTeamA = (Button) rootView.findViewById(R.id.decPointsForTeamA);
        decPointsForTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decPointsForTeamA();
            }
        });

        final Button addPointsForTeamB = (Button) rootView.findViewById(R.id.addPointsForTeamB);
        addPointsForTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointsForTeamB();
            }
        });

        final Button decPointsForTeamB = (Button) rootView.findViewById(R.id.decPointsForTeamB);
        decPointsForTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decPointsForTeamB();
            }
        });


        final Button setTask = (Button) rootView.findViewById(R.id.setTaskButton);
        setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskImage();
                if (isTeamATurn) Toast.makeText(GameActivity.getGameContext(), getNextPlayerTeamA(), Toast.LENGTH_SHORT).show();
                else Toast.makeText(GameActivity.getGameContext(), getNextPlayerTeamB(), Toast.LENGTH_SHORT).show();
                isTeamATurn = !isTeamATurn;
            }
        });

        initListTeam(PlayerEntry.TEAM_A);
        initListTeam(PlayerEntry.TEAM_B);

        return rootView;
    }

    private void initLists() {
        playerListOfTeamB.clear();
        playerListOfTeamA.clear();
        playerPointsOfTeamA.clear();
        playerPointsOfTeamB.clear();
    }

    private void setTeamNames() {

        try(Cursor cursor = GameActivity.getGameContext().getContentResolver().query(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), null, null, null, null)) {
            if (cursor != null && cursor.moveToNext()) {
                nameOfTeamA = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME_TEAM_A));
                nameOfTeamB = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME_TEAM_B));
                return;
            }
        }

        nameOfTeamA = "Team A";
        nameOfTeamB = "Team B";
    }

    private void initListTeam(int teamValue) {
        String[] projection = {
                PlayerEntry.COLUMN_GAME_ID,
                PlayerEntry.COLUMN_PLAYER_NAME,
                PlayerEntry.COLUMN_PLAYER_POINTS
        };
        //String selection = PlayerEntry.COLUMN_GAME_ID + " = ? AND " + PlayerEntry.COLUMN_PLAYER_TEAM + " = ?";
        String selection = PlayerEntry.COLUMN_PLAYER_TEAM + " = ?";
        //String selectionArgs[] = { String.valueOf(gameId), String.valueOf(teamValue) };
        String selectionArgs[] = { String.valueOf(teamValue) };
        //Cursor cursor = GameActivity.getGameContext().getContentResolver().query(PlayerEntry.CONTENT_URI, projection, null, null, PlayerEntry._ID);

        try(Cursor cursor = GameActivity.getGameContext().getContentResolver().query(PlayerEntry.CONTENT_URI, projection, selection, selectionArgs, PlayerEntry._ID)) {
            if (teamValue == PlayerEntry.TEAM_A) {
                while (cursor != null && cursor.moveToNext()) {
                    playerListOfTeamA.add(cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)) + " " +
                    cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_GAME_ID)));
                    playerPointsOfTeamA.add(cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS)));
                }
                return;
            }
            while (cursor != null && cursor.moveToNext()) {
                playerListOfTeamB.add(cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)) + " " +
                        cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_GAME_ID)));
                playerPointsOfTeamB.add(cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS)));
            }
        }
    }

    private void updateTeamNameViews() {
        TextView teamNameAView = (TextView) rootView.findViewById(R.id.teamANameView);
        teamNameAView.setText(nameOfTeamA);

        TextView teamNameBView = (TextView) rootView.findViewById(R.id.teamBNameView);
        teamNameBView.setText(nameOfTeamB);
    }

    private void addPointsForTeamA() {
        TextView teamAScoreView = (TextView) getView().findViewById(R.id.scoreOfTeamA);
        int newScore = Integer.parseInt(teamAScoreView.getText().toString()) + currentPointsOfTask();
        teamAScoreView.setText(String.valueOf(newScore));
    }

    private void decPointsForTeamA() {
        TextView teamAScoreView = (TextView) getView().findViewById(R.id.scoreOfTeamA);
        int newScore = Integer.parseInt(teamAScoreView.getText().toString()) - currentPointsOfTask();
        if (Integer.parseInt(teamAScoreView.getText().toString()) >= currentPointsOfTask()) teamAScoreView.setText(String.valueOf(newScore));
    }

    private void addPointsForTeamB() {
        TextView teamBScoreView = (TextView) getView().findViewById(R.id.scoreOfTeamB);
        int newScore = Integer.parseInt(teamBScoreView.getText().toString()) + currentPointsOfTask();
        teamBScoreView.setText(String.valueOf(newScore));
    }

    private void decPointsForTeamB() {
        TextView teamBScoreView = (TextView) getView().findViewById(R.id.scoreOfTeamB);
        int newScore = Integer.parseInt(teamBScoreView.getText().toString()) - currentPointsOfTask();
        if (Integer.parseInt(teamBScoreView.getText().toString()) >= currentPointsOfTask()) teamBScoreView.setText(String.valueOf(newScore));
    }

    private void updatePointsForTeamA(int playerPoints) {
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_POINTS, playerPoints);
        GameActivity.getGameContext().getContentResolver().update(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), values, null, null);
    }

    private int currentPointsOfTask() {
        switch(currentTask) {
            case 1:
                return 3;
            case 4:
            case 5:
            case 6:
                return 2;
            case 2:
            case 3:
            case 7:
            case 8:
            case 9:
            case 10:
                return 1;
            default:
                return 0;
        }
    }

    public void setTaskImage() {
        currentTask = (int) (Math.random() * 10 + 1);
        ImageView imgView = (ImageView) getView().findViewById(R.id.taskImageView);
        TextView taskText = (TextView) getView().findViewById(R.id.taskTextView);
        switch (currentTask) {
            case 1 : {
                imgView.setImageResource(R.drawable.blind);
                taskText.setText(R.string.task_blind);
                break;
            }
            case 2 : case 3 : {
                imgView.setImageResource(R.drawable.ratte);
                taskText.setText(R.string.task_ratte);
                break;
            }
            case 4 : case 5 : case 6 : {
                imgView.setImageResource(R.drawable.duell);
                taskText.setText(R.string.task_duell);
                break;
            }
            case 7 : case 8 : {
                imgView.setImageResource(R.drawable.wasserballon);
                taskText.setText(R.string.task_wasserballon);
                break;
            }
            case 9 : case 10 : {
                imgView.setImageResource(R.drawable.gold);
                taskText.setText(R.string.task_gold);
                break;
            }
        }
    }

    public String getNextPlayerTeamA() {
        if (++playerTurnCounterOfTeamA == playerListOfTeamA.size()) playerTurnCounterOfTeamA = 0;
        Log.v("playerName", playerListOfTeamA.get(playerTurnCounterOfTeamA));
        return playerListOfTeamA.get(playerTurnCounterOfTeamA);
    }

    public String getNextPlayerTeamB() {
        if (++playerTurnCounterOfTeamB == playerListOfTeamB.size()) playerTurnCounterOfTeamB = 0;
        Log.v("playerName", playerListOfTeamB.get(playerTurnCounterOfTeamB));
        return playerListOfTeamB.get(playerTurnCounterOfTeamB);
    }
}
