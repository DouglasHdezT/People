package com.debugps.people.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStatePagerAdapter{

    private ArrayList<Fragment> fragments= new ArrayList<>();
    private ArrayList<String> titles= new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void addFragmentToList(Fragment frag, String title){
        fragments.add(frag);
        titles.add(title);
    }
}
