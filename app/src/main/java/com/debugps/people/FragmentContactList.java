package com.debugps.people;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentContactList extends Fragment {

    private int id_type_of_fragment;
    private RecyclerView rv;

    public FragmentContactList() {}

    public static FragmentContactList newInstance(int id_type_of_fragment){
        FragmentContactList frag = new FragmentContactList();

        frag.setId_type_of_fragment(id_type_of_fragment);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setId_type_of_fragment(int id_type_of_fragment) {
        this.id_type_of_fragment = id_type_of_fragment;
    }

    public interface OnBindAdapter{
        void OnBindAdapter(RecyclerView rv, int id_type_of_fragment);
    }
}
