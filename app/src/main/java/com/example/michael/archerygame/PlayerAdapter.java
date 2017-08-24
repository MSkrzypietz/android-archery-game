package com.example.michael.archerygame;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        playerNameView.setText(currentPlayer.getName());

        TextView playerScoreView = (TextView) listItemView.findViewById(R.id.list_item_player_score);
        playerScoreView.setText(String.valueOf(currentPlayer.getScore()));

        return listItemView;
    }
}
