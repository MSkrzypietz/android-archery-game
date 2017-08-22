package com.example.michael.archerygame;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    public static long gameId;
    public static Context gameContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        gameId = getIntent().getLongExtra("GAME_ID", -1);
        gameContext = getApplicationContext();
    }

    public static Context getGameContext() {
        return gameContext;
    }
}
