package com.example.michael.archerygame;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michael.archerygame.data.GameContract.GameEntry;
import com.example.michael.archerygame.data.PlayerContract.PlayerEntry;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    public static String nameOfTeamA;
    public static String nameOfTeamB;

    private long gameId;
    public static ArrayList<Player> playerListOfTeamA = new ArrayList<>();
    public static ArrayList<Player> playerListOfTeamB = new ArrayList<>();
    private int playerTurnCounterOfTeamA = 0;
    private int playerTurnCounterOfTeamB = 0;
    private int currentTask;
    private Player currentPlayer;
    private View rootView;
    private int teamTurn = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_task, container, false);

        clearLists();
        gameId = GameActivity.gameId;
        if (gameId != -1) {
            initGameDetails();
            updateTeamNameViews();
        }

        final Button addPointsForTeamA = (Button) rootView.findViewById(R.id.addPointsForTeamA);
        final Button addPointsForTeamB = (Button) rootView.findViewById(R.id.addPointsForTeamB);
        final Button decPointsForTeamA = (Button) rootView.findViewById(R.id.decPointsForTeamA);
        final Button decPointsForTeamB = (Button) rootView.findViewById(R.id.decPointsForTeamB);

        addPointsForTeamA.setEnabled(false);
        addPointsForTeamB.setEnabled(false);
        decPointsForTeamA.setEnabled(false);
        decPointsForTeamB.setEnabled(false);

        addPointsForTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointsForTeamA();
                addPointsForTeamA.setEnabled(false);
                decPointsForTeamA.setEnabled(true);
            }
        });


        decPointsForTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decPointsForTeamA();
                addPointsForTeamA.setEnabled(true);
                decPointsForTeamA.setEnabled(false);
            }
        });

        addPointsForTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointsForTeamB();
                addPointsForTeamB.setEnabled(false);
                decPointsForTeamB.setEnabled(true);
            }
        });

        decPointsForTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decPointsForTeamB();
                addPointsForTeamB.setEnabled(true);
                decPointsForTeamB.setEnabled(false);
            }
        });

        final Button setTask = (Button) rootView.findViewById(R.id.setTaskButton);
        setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTaskImage();
                if (teamTurn == GameEntry.TEAM_A) {
                    addPointsForTeamA.setEnabled(true);
                    addPointsForTeamB.setEnabled(false);

                    currentPlayer = getNextPlayerTeamA();
                    Toast.makeText(getActivity(), currentPlayer.getName(), Toast.LENGTH_SHORT).show();
                }
                else {
                    addPointsForTeamA.setEnabled(false);
                    addPointsForTeamB.setEnabled(true);

                    currentPlayer = getNextPlayerTeamB();
                    Toast.makeText(getActivity(), currentPlayer.getName(), Toast.LENGTH_SHORT).show();
                }
                decPointsForTeamA.setEnabled(false);
                decPointsForTeamB.setEnabled(false);

                teamTurn = teamTurn == GameEntry.TEAM_A ? GameEntry.TEAM_B : GameEntry.TEAM_A;
                updateTurnCounters();
            }
        });

        initListTeam(PlayerEntry.TEAM_A);
        initListTeam(PlayerEntry.TEAM_B);
        updateScoreOfTeamAView();
        updateScoreOfTeamBView();

        return rootView;
    }

    private void clearLists() {
        playerListOfTeamB.clear();
        playerListOfTeamA.clear();
    }

    private void initGameDetails() {

        try (Cursor cursor = getActivity().getContentResolver().query(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), null, null, null, null)) {
            if (cursor != null && cursor.moveToNext()) {
                nameOfTeamA = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME_TEAM_A));
                nameOfTeamB = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GAME_NAME_TEAM_B));
                playerTurnCounterOfTeamA = cursor.getInt(cursor.getColumnIndex(GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_A));
                playerTurnCounterOfTeamB = cursor.getInt(cursor.getColumnIndex(GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_B));
                teamTurn = cursor.getInt(cursor.getColumnIndex(GameEntry.COLUMN_GAME_TEAM_TURN));
                return;
            }
        }

        nameOfTeamA = "Team A";
        nameOfTeamB = "Team B";
    }

    private void initListTeam(int teamValue) {
        String[] projection = {
                PlayerEntry._ID,
                PlayerEntry.COLUMN_GAME_ID,
                PlayerEntry.COLUMN_PLAYER_NAME,
                PlayerEntry.COLUMN_PLAYER_IS_PLAYING,
                PlayerEntry.COLUMN_PLAYER_POINTS
        };

        String selection = PlayerEntry.COLUMN_PLAYER_TEAM + " = ? AND " + PlayerEntry.COLUMN_GAME_ID + " = ?";
        String selectionArgs[] = { String.valueOf(teamValue), String.valueOf(gameId) };

        try (Cursor cursor = getActivity().getContentResolver().query(PlayerEntry.CONTENT_URI, projection, selection, selectionArgs, PlayerEntry._ID)) {
            if (teamValue == PlayerEntry.TEAM_A) {

                while (cursor != null && cursor.moveToNext()) {
                    boolean isPlaying = cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_IS_PLAYING)) == PlayerEntry.IS_PLAYING;
                    playerListOfTeamA.add(new Player(
                            cursor.getLong(cursor.getColumnIndex(PlayerEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)),
                            cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS)),
                            isPlaying));
                }
                return;
            }
            while (cursor != null && cursor.moveToNext()) {
                boolean isPlaying = cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_IS_PLAYING)) == PlayerEntry.IS_PLAYING;
                playerListOfTeamB.add(new Player(
                        cursor.getLong(cursor.getColumnIndex(PlayerEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)),
                        cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS)),
                        isPlaying));
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
        int newScore = currentPlayer.getScore() + getPointsOfCurrentTask();
        currentPlayer.setScore(newScore);
        updateScoreOfTeamAView();
        updatePointsOfCurrentPlayer();
    }

    private void decPointsForTeamA() {
        int newScore = currentPlayer.getScore() - getPointsOfCurrentTask();
        if (newScore < 0) return;

        currentPlayer.setScore(newScore);
        updateScoreOfTeamAView();
        updatePointsOfCurrentPlayer();
    }

    private void addPointsForTeamB() {
        int newScore = currentPlayer.getScore() + getPointsOfCurrentTask();
        currentPlayer.setScore(newScore);
        updateScoreOfTeamBView();
        updatePointsOfCurrentPlayer();
    }

    private void decPointsForTeamB() {
        int newScore = currentPlayer.getScore() - getPointsOfCurrentTask();
        if (newScore < 0) return;

        currentPlayer.setScore(newScore);
        updateScoreOfTeamBView();
        updatePointsOfCurrentPlayer();
    }

    private void updateScoreOfTeamAView() {
        int pointsOfTeamA = calcPointsOfTeamA();
        TextView teamAScoreView = (TextView) rootView.findViewById(R.id.scoreOfTeamA);
        teamAScoreView.setText(String.valueOf(pointsOfTeamA));
    }

    private int calcPointsOfTeamA() {
        int pointsOfTeamA = 0;
        for (Player player : playerListOfTeamA) {
            pointsOfTeamA += player.getScore();
        }
        return pointsOfTeamA;
    }

    private void updateScoreOfTeamBView() {
        int pointsOfTeamB = calcPointsOfTeamB();
        TextView teamBScoreView = (TextView) rootView.findViewById(R.id.scoreOfTeamB);
        teamBScoreView.setText(String.valueOf(pointsOfTeamB));
    }

    private int calcPointsOfTeamB() {
        int pointsOfTeamB = 0;
        for (Player player : playerListOfTeamB) {
            pointsOfTeamB += player.getScore();
        }
        return pointsOfTeamB;
    }

    private void updatePointsOfCurrentPlayer() {
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_POINTS, getPointsOfCurrentPlayer());
        getActivity().getContentResolver().update(ContentUris.withAppendedId(PlayerEntry.CONTENT_URI, currentPlayer.getPlayerId()), values, null, null);
    }

    private int getPointsOfCurrentPlayer() {
        return currentPlayer.getScore();
    }

    private int getPointsOfCurrentTask() {
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
        ImageView imgView = (ImageView) rootView.findViewById(R.id.taskImageView);
        TextView taskText = (TextView) rootView.findViewById(R.id.taskTextView);
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

    public Player getNextPlayerTeamA() {
        if (++playerTurnCounterOfTeamA == playerListOfTeamA.size()) playerTurnCounterOfTeamA = 0;
        Player player = playerListOfTeamA.get(playerTurnCounterOfTeamA);
        if (player.getIsPlaying()) return player;
        return getNextPlayerTeamA();
    }

    public Player getNextPlayerTeamB() {
        if (++playerTurnCounterOfTeamB == playerListOfTeamB.size()) playerTurnCounterOfTeamB = 0;
        Player player = playerListOfTeamB.get(playerTurnCounterOfTeamB);
        if (player.getIsPlaying()) return player;
        return getNextPlayerTeamB();
    }

    public static ArrayList<Player> getPlayerListOfTeamA() {
        return new ArrayList<>(playerListOfTeamA);
    }

    public static ArrayList<Player> getPlayerListOfTeamB() {
        return new ArrayList<>(playerListOfTeamB);
    }

    private void updateTurnCounters() {
        ContentValues values = new ContentValues();
        values.put(GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_A, playerTurnCounterOfTeamA);
        values.put(GameEntry.COLUMN_GAME_TURN_COUNTER_TEAM_B, playerTurnCounterOfTeamB);
        values.put(GameEntry.COLUMN_GAME_TEAM_TURN,
                teamTurn == GameEntry.TEAM_A ? GameEntry.TEAM_A : GameEntry.TEAM_B);
        getActivity().getContentResolver().update(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), values, null, null);
    }
}