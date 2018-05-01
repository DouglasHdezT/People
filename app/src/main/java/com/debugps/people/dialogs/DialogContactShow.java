package com.debugps.people.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogContactShow extends DialogFragment {

    private Contact contact;

    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView birth;

    private ImageView profilePhoto;
    private CircleImageView shareButton;
    private CircleImageView callButton;


    public DialogContactShow() {
    }

    public static DialogContactShow newInstance(Contact contact){
        DialogContactShow dialogContactShow = new DialogContactShow();
        dialogContactShow.setContact(contact);
        return dialogContactShow;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contact == null){
            return null;
        }
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


        View view = inflater.inflate(R.layout.dialog_contact_show,container,false);

        view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        view.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

        name = view.findViewById(R.id.dialog_name);
        email = view.findViewById(R.id.dialog_email);
        phone = view.findViewById(R.id.dialog_phone);
        birth = view.findViewById(R.id.dialog_birth);

        profilePhoto = view.findViewById(R.id.dialog_profile_photo);
        shareButton = view.findViewById(R.id.dialog_share);
        callButton = view.findViewById(R.id.dialog_call);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhoneNumbers());
        birth.setText(contact.getBirthday());

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        return d;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
