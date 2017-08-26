package com.example.michael.archerygame;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements InsertPlayerDialogFragment.InsertPlayerDialogListener {

    public static long gameId;
    private SimpleFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        gameId = getIntent().getLongExtra("GAME_ID", -1);
    }

    @Override
    public void updateAdapters() {
        //ScoreFragment.updateItemAdapters(adapter.getItem(1).getView());
    }

}
