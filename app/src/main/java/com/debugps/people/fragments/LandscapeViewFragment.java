package com.debugps.people.fragments;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;
import com.debugps.people.intefaces.OnSettingContact;

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

    private LinearLayout phonesLayout;

    private OnSettingContact onSettingContact;

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

        profilePhoto =  view.findViewById(R.id.layout_landscape_profile_photo);
        shareButton = view.findViewById(R.id.layout_landscape_share);
        callButton = view.findViewById(R.id.layout_landscape_call);

        name.setText(contact.getName());
        birth.setText(contact.getBirthday());
        email.setText(contact.getEmail());

        phonesLayout = view.findViewById(R.id.linear_layout_phones_parent);

        for(int i=0;i<contact.getPhoneNumbers().size();i++){
            LinearLayout viewEditText = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_phones_landscape, phonesLayout,false);
            TextView txt = viewEditText.findViewById(R.id.dialog_phone_title);
            TextView txt2 = viewEditText.findViewById(R.id.dialog_phone_child);
            String phoneN = contact.getPhoneNumber(i);
            String txt_default= getString(R.string.parcial_phone_text) +" "+ (i+1)+ ": ";

            if(i>=1){
                txt.setText(txt_default);
            }else{
                txt.setText(R.string.card_view_phone_number_text);
            }

            txt2.setText(phoneN);

            phonesLayout.addView(viewEditText);
        }

        if(contact.getProfileImage() == null){
            profilePhoto.setImageResource(R.drawable.ic_person);
            profilePhoto.setBackgroundResource(contact.getColorId());
        }else{
            profilePhoto.setImageURI(contact.getProfileImage());
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingContact.callContact(contact);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSettingContact){
            onSettingContact = (OnSettingContact) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSettingContact = null;
    }

}
