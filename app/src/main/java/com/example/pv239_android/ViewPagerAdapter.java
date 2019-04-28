package com.example.pv239_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();
    private static final String TAG = "ViewPagerAdapter";


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TabFragment tabFragment = (TabFragment) fragmentList.get(position);
        position = position + 1;
        Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment: " + position);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public int getCount() {
        return fragmentTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    public void addFragment(TabFragment newFragment, String title) {
        newFragment.setTitle(title);
        fragmentList.add(newFragment);
        fragmentTitles.add(title);
    }

    @Override
    public void notifyDataSetChanged() {
        ((TabFragment) fragmentList.get(0)).dataChanged();
    }
}
