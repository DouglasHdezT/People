package com.debugps.people.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandscapeViewFragment extends Fragment {

    private Contact contact;

    private TextView name;
    private TextView phone;
    private TextView email;
    private TextView birth;

    private ImageView profilePhoto;
    private CircleImageView shareButton;
    private CircleImageView callButton;

    public LandscapeViewFragment() {
    }

    public static LandscapeViewFragment newInstance(Contact contact){
        LandscapeViewFragment landscapeViewFragment = new LandscapeViewFragment();
        landscapeViewFragment.setContact(contact);
        return landscapeViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contact == null){
            return null;
        }

        View view= inflater.inflate(R.layout.layout_contact_show_landscape,container, false);

        name = view.findViewById(R.id.layout_landscape_name);
        birth = view.findViewById(R.id.layout_landscape_birth);
        email = view.findViewById(R.id.layout_landscape_email);
        phone = view.findViewById(R.id.layout_landscape_phone);

        profilePhoto =  view.findViewById(R.id.layout_landscape_profile_photo);
        shareButton = view.findViewById(R.id.layout_landscape_share);
        callButton = view.findViewById(R.id.layout_landscape_call);

        name.setText(contact.getName());
        birth.setText(contact.getBirthday());
        phone.setText(contact.getPhoneNumber(0));
        email.setText(contact.getEmail());

        if(contact.getProfileImage() == null){
            profilePhoto.setImageResource(R.drawable.ic_person);
            profilePhoto.setBackgroundResource(contact.getColorId());
        }else{
            profilePhoto.setImageBitmap(contact.getProfileImage());
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.callContact(getContext(),contact);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.shareContact(getContext(),contact);
            }
        });

        return view;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
