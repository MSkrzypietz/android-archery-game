package com.example.michael.archerygame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_score, container, false);

        updateTeamNameViews();

        PlayerAdapter itemsAdapterTeamA = new PlayerAdapter(getActivity(), TaskFragment.playerListOfTeamA);
        ListView listViewTeamA = (ListView) rootView.findViewById(R.id.playerListOfTeamA);
        listViewTeamA.setAdapter(itemsAdapterTeamA);

        PlayerAdapter itemsAdapterTeamB = new PlayerAdapter(getActivity(), TaskFragment.playerListOfTeamB);
        ListView listViewTeamB = (ListView) rootView.findViewById(R.id.playerListOfTeamB);
        listViewTeamB.setAdapter(itemsAdapterTeamB);

        return rootView;
    }

    private void updateTeamNameViews() {
        TextView teamNameAView = (TextView) rootView.findViewById(R.id.teamANameView);
        teamNameAView.setText(TaskFragment.nameOfTeamA);

        TextView teamNameBView = (TextView) rootView.findViewById(R.id.teamBNameView);
        teamNameBView.setText(TaskFragment.nameOfTeamB);
    }
}
