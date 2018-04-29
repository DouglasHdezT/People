package com.debugps.people.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.adapters.MainPagerAdapter;

public class MainFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MainPagerAdapter pagerAdapter;


    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagerAdapter = new MainPagerAdapter(getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab_contact_list_layout, container, false);

        mTabLayout = view.findViewById(R.id.tablayout_main);
        mViewPager = view.findViewById(R.id.viewpager_main);

        ContactListFragment contactListFragment_default = ContactListFragment.newInstance(MainActivity.ID_DEFAULT_KEY);
        ContactListFragment contactListFragment_favorites = ContactListFragment.newInstance(MainActivity.ID_FAV_KEY);

        pagerAdapter.addFragmentToList(contactListFragment_default,"Contactos");
        pagerAdapter.addFragmentToList(contactListFragment_favorites,"Favorites");

        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

}
