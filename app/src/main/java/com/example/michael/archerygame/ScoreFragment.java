package com.example.michael.archerygame;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Comparator;

public class ScoreFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_score, container, false);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
*/
        updateTeamNameViews();
        updateItemAdapters(getActivity());

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible){
            updateItemAdapters(getActivity());
        }
    }

    private void updateTeamNameViews() {
        TextView teamNameAView = (TextView) rootView.findViewById(R.id.teamANameView);
        teamNameAView.setText(TaskFragment.nameOfTeamA);

        TextView teamNameBView = (TextView) rootView.findViewById(R.id.teamBNameView);
        teamNameBView.setText(TaskFragment.nameOfTeamB);
    }

    private void updateItemAdapters(Activity context) {
        Comparator<Player> scoreComparator = new Comparator<Player>() {
            @Override
            public int compare(Player player, Player t1) {
                return t1.getScore() - player.getScore();
            }
        };
        updateItemsAdapterOfTeamA(context, scoreComparator);
        updateItemsAdapterOfTeamB(context, scoreComparator);
    }

    private void updateItemsAdapterOfTeamA(Activity context, Comparator<Player> scoreComparator) {
        PlayerAdapter itemsAdapterTeamA = new PlayerAdapter(context, TaskFragment.getPlayerListOfTeamA());
        itemsAdapterTeamA.sort(scoreComparator);
        ListView listViewTeamA = (ListView) rootView.findViewById(R.id.playerListOfTeamA);
        listViewTeamA.setAdapter(itemsAdapterTeamA);
    }

    private void updateItemsAdapterOfTeamB(Activity context, Comparator<Player> scoreComparator) {
        PlayerAdapter itemsAdapterTeamB = new PlayerAdapter(context, TaskFragment.getPlayerListOfTeamB());
        itemsAdapterTeamB.sort(scoreComparator);
        ListView listViewTeamB = (ListView) rootView.findViewById(R.id.playerListOfTeamB);
        listViewTeamB.setAdapter(itemsAdapterTeamB);
    }
}
