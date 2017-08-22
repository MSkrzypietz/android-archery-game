package com.example.michael.archerygame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Michael on 07.08.2017.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Spiel", "Tabelle"};

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TaskFragment();
        } else {
            return new ScoreFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
