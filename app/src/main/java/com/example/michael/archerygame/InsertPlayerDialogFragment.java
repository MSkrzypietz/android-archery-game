package com.example.michael.archerygame;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.michael.archerygame.data.PlayerContract;

import static com.example.michael.archerygame.GameActivity.gameId;

public class InsertPlayerDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_insert_player, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                RadioGroup teamSelection = (RadioGroup) view.findViewById(R.id.teamSelectionRadioGroup);
                if (teamSelection.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Bitt wähle ein Team aus", Toast.LENGTH_SHORT).show();
                    return;
                }
                int selectedId = teamSelection.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) view.findViewById(selectedId);
                String teamName = selectedRadioButton.getText().toString();
                Toast.makeText(getContext(), teamName, Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                InsertPlayerDialogFragment.this.getDialog().cancel();
            }
        });

        updateRadioButtonsText(view);

        return builder.create();
    }

    private void updateRadioButtonsText(View view) {
        RadioButton teamARadioButton = (RadioButton) view.findViewById(R.id.teamARadioButton);
        teamARadioButton.setText(TaskFragment.nameOfTeamA);

        RadioButton teamBRadioButton = (RadioButton) view.findViewById(R.id.teamBRadioButton);
        teamBRadioButton.setText(TaskFragment.nameOfTeamB);
    }

    private void createPlayerTableEntries(Activity context, int teamValue) {
        ContentValues values = new ContentValues();
        //#values.put(PlayerContract.PlayerEntry.COLUMN_PLAYER_NAME, player);
        values.put(PlayerContract.PlayerEntry.COLUMN_PLAYER_TEAM, teamValue);
        values.put(PlayerContract.PlayerEntry.COLUMN_GAME_ID, gameId);

        long playerId = ContentUris.parseId(context.getContentResolver().insert(PlayerContract.PlayerEntry.CONTENT_URI, values));
    }
}
