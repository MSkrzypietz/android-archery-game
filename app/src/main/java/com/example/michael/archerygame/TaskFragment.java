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

@SuppressWarnings("ConstantConditions")
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
    private boolean isTeamATurn = true;

    //TODO: playerTurnCounter has to be saved in the db as well

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
                if (isTeamATurn) {
                    addPointsForTeamA.setEnabled(true);
                    addPointsForTeamB.setEnabled(false);
                }
                else {
                    addPointsForTeamA.setEnabled(false);
                    addPointsForTeamB.setEnabled(true);
                }
                decPointsForTeamA.setEnabled(false);
                decPointsForTeamB.setEnabled(false);
                if (isTeamATurn) {
                    currentPlayer = getNextPlayerTeamA();
                    Toast.makeText(GameActivity.getGameContext(), currentPlayer.getName(), Toast.LENGTH_SHORT).show();
                }
                else {
                    currentPlayer = getNextPlayerTeamB();
                    Toast.makeText(GameActivity.getGameContext(), currentPlayer.getName(), Toast.LENGTH_SHORT).show();
                }
                isTeamATurn = !isTeamATurn;
            }
        });

        initListTeam(PlayerEntry.TEAM_A);
        initListTeam(PlayerEntry.TEAM_B);
        updateScoreOfTeamAView();
        updateScoreOfTeamBView();

        return rootView;
    }

    private void initLists() {
        playerListOfTeamB.clear();
        playerListOfTeamA.clear();
    }

    private void setTeamNames() {

        try (Cursor cursor = GameActivity.getGameContext().getContentResolver().query(ContentUris.withAppendedId(GameEntry.CONTENT_URI, gameId), null, null, null, null)) {
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
                PlayerEntry._ID,
                PlayerEntry.COLUMN_GAME_ID,
                PlayerEntry.COLUMN_PLAYER_NAME,
                PlayerEntry.COLUMN_PLAYER_POINTS
        };

        String selection = PlayerEntry.COLUMN_PLAYER_TEAM + " = ? AND " + PlayerEntry.COLUMN_GAME_ID + " = ?";
        String selectionArgs[] = { String.valueOf(teamValue), String.valueOf(gameId) };

        try (Cursor cursor = GameActivity.getGameContext().getContentResolver().query(PlayerEntry.CONTENT_URI, projection, selection, selectionArgs, PlayerEntry._ID)) {
            if (teamValue == PlayerEntry.TEAM_A) {

                while (cursor != null && cursor.moveToNext()) {
                    playerListOfTeamA.add(new Player(
                            cursor.getLong(cursor.getColumnIndex(PlayerEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)),
                            cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS))));
                }
                return;
            }
            while (cursor != null && cursor.moveToNext()) {
                playerListOfTeamB.add(new Player(
                        cursor.getLong(cursor.getColumnIndex(PlayerEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME)),
                        cursor.getInt(cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_POINTS))));
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
        GameActivity.getGameContext().getContentResolver().update(ContentUris.withAppendedId(PlayerEntry.CONTENT_URI, currentPlayer.getPlayerId()), values, null, null);
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

    public Player getNextPlayerTeamA() {
        if (++playerTurnCounterOfTeamA == playerListOfTeamA.size()) playerTurnCounterOfTeamA = 0;
        return playerListOfTeamA.get(playerTurnCounterOfTeamA);
    }

    public Player getNextPlayerTeamB() {
        if (++playerTurnCounterOfTeamB == playerListOfTeamB.size()) playerTurnCounterOfTeamB = 0;
        return playerListOfTeamB.get(playerTurnCounterOfTeamB);
    }

    public static ArrayList<Player> getPlayerListOfTeamA() {
        return new ArrayList<>(playerListOfTeamA);
    }

    public static ArrayList<Player> getPlayerListOfTeamB() {
        return new ArrayList<>(playerListOfTeamB);
    }
}