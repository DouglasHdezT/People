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
import com.debugps.people.dialogs.DialogContactShow;
import com.debugps.people.intefaces.OnSettingContact;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandscapeViewFragment extends Fragment {

    private Contact contact;

    private TextView name;

    private ImageView profilePhoto;
    private CircleImageView shareButton;
    private CircleImageView callButton;
    private CircleImageView favButton;
    private CircleImageView removeButton;
    private CircleImageView editButton;

    private LinearLayout phonesLayout;
    private LinearLayout emailLayout;
    private LinearLayout birthLayout;

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

        profilePhoto =  view.findViewById(R.id.layout_landscape_profile_photo);
        shareButton = view.findViewById(R.id.layout_landscape_share);
        callButton = view.findViewById(R.id.layout_landscape_call);

        removeButton = view.findViewById(R.id.landscape_remove_button);
        favButton = view.findViewById(R.id.landscape_fav_button);
        editButton = view.findViewById(R.id.landscape_edit_button);

        phonesLayout = view.findViewById(R.id.linear_layout_phones_parent);
        emailLayout = view.findViewById(R.id.linear_layout_email_parent);
        birthLayout = view.findViewById(R.id.linear_layout_birthday_parent);

        name.setText(contact.getName());

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

        if(contact.getEmail() != null){
            if(!contact.getEmail().equals("")){
                LinearLayout viewEmails = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_phones_landscape, null);
                TextView title =  viewEmails.findViewById(R.id.dialog_phone_title);
                TextView content =  viewEmails.findViewById(R.id.dialog_phone_child);
                title.setText(R.string.edit_text_email);
                content.setText(contact.getEmail());
                emailLayout.addView(viewEmails);
            }
        }

        if(contact.getBirthday()!= null){
            if(!contact.getBirthday().equals("")){
                LinearLayout viewBirth = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_phones_landscape, null);
                TextView title =  viewBirth.findViewById(R.id.dialog_phone_title);
                TextView content =  viewBirth.findViewById(R.id.dialog_phone_child);
                title.setText(R.string.edit_text_birth);
                content.setText(contact.getBirthday());
                emailLayout.addView(viewBirth);
            }
        }

        if(contact.isFavorite()){
            favButton.setCircleBackgroundColorResource(R.color.star_color);
        }else{
            favButton.setCircleBackgroundColorResource(R.color.MaterialBlueGrey900);
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

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingContact.removeContact(contact);
                FragmentTransaction ft;
                if (getFragmentManager() != null) {
                    ft = getFragmentManager().beginTransaction();
                    ft.remove(LandscapeViewFragment.this);
                    ft.commit();
                }
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact.isFavorite()){
                    onSettingContact.unsetFavorited(contact);
                    favButton.setCircleBackgroundColorResource(R.color.MaterialBlueGrey900);
                }else{
                    onSettingContact.setFavorited(contact);
                    favButton.setCircleBackgroundColorResource(R.color.star_color);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingContact.editContact(contact);
                FragmentTransaction ft;
                if (getFragmentManager() != null) {
                    ft = getFragmentManager().beginTransaction();
                    ft.remove(LandscapeViewFragment.this);
                    ft.commit();
                }
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
