package com.example.michael.archerygame;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

    public PlayerAdapter(Activity context, ArrayList<Player> playerList) {
        super(context, 0, playerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Player currentPlayer = getItem(position);

        TextView playerNameView = (TextView) listItemView.findViewById(R.id.list_item_player_name);
        TextView playerScoreView = (TextView) listItemView.findViewById(R.id.list_item_player_score);
        if (currentPlayer == null) return listItemView;

        playerNameView.setText(currentPlayer.getName());
        playerScoreView.setText(String.valueOf(currentPlayer.getScore()));

        if (!currentPlayer.getIsPlaying()) {
            listItemView.setEnabled(false);
            playerNameView.setPaintFlags(playerNameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            playerScoreView.setPaintFlags(playerScoreView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return listItemView;
    }

}
