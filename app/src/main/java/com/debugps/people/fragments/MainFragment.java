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
import com.debugps.people.data.Contact;

import java.util.ArrayList;

public class MainFragment extends Fragment{

    private ArrayList<Contact> contacts_list;
    private ArrayList<Contact> contactsFav_list;
    private ArrayList<Contact> contactsRecent_list;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MainPagerAdapter pagerAdapter;


    public MainFragment() {
    }

    public static MainFragment newInstance(){
        MainFragment frag =  new MainFragment();


        return frag;
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

        FragmentContactList fragmentContactList_default = FragmentContactList.newInstance(MainActivity.ID_DEFAULT_KEY);

        pagerAdapter.addFragmentToList(fragmentContactList_default,"");

        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void setContacts_list(ArrayList<Contact> contacts_list) {
        this.contacts_list = contacts_list;
    }

    public void setContactsFav_list(ArrayList<Contact> contactsFav_list) {
        this.contactsFav_list = contactsFav_list;
    }

    public void setContactsRecent_list(ArrayList<Contact> contactsRecent_list) {
        this.contactsRecent_list = contactsRecent_list;
    }
}
