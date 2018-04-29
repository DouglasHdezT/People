package com.debugps.people.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugps.people.R;

public class ContactListFragment extends Fragment {

    private int id_type_of_fragment;

    private RecyclerView rv;

    //Interfaz Que permite settear los adpters desde el mainFragment
    private OnBindAdapter binderAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public ContactListFragment() {}

    public static ContactListFragment newInstance(int id_type_of_fragment){
        ContactListFragment frag = new ContactListFragment();
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
        View view = inflater.inflate(R.layout.recycle_view_contacts_list, container, false);

        rv = view.findViewById(R.id.recycle_view_main);
        linearLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(),2);

        rv.setHasFixedSize(true);

        binderAdapter.OnBindAdapter(rv,id_type_of_fragment, linearLayoutManager, gridLayoutManager);
        return view;
    }

    public void setId_type_of_fragment(int id_type_of_fragment) {
        this.id_type_of_fragment = id_type_of_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnBindAdapter){
            binderAdapter = (OnBindAdapter) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binderAdapter = null;
    }

    public interface OnBindAdapter{
        void OnBindAdapter(RecyclerView rv, int id_type_of_fragment, LinearLayoutManager l, GridLayoutManager g);
    }
}
