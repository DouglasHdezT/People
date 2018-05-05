package com.debugps.people.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.debugps.people.MainActivity;
import com.debugps.people.R;
import com.debugps.people.data.Contact;
import com.debugps.people.intefaces.OnSettingContact;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogContactShow extends DialogFragment {

    private Contact contact;

    private TextView name;
    private TextView email;
    private TextView birth;

    private ImageView profilePhoto;
    private CircleImageView shareButton;
    private CircleImageView callButton;

    private CircleImageView closeButton;
    private CircleImageView editButton;
    private CircleImageView removeButton;
    private CircleImageView favButton;

    private LinearLayout phonesLayout;

    private OnSettingContact onSettingContact;


    public DialogContactShow() {
    }

    public static DialogContactShow newInstance(Contact contact) {
        DialogContactShow dialogContactShow = new DialogContactShow();
        dialogContactShow.setContact(contact);
        return dialogContactShow;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contact == null) {
            return null;
        }
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


        View view = inflater.inflate(R.layout.dialog_contact_show, container, false);

        view.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        view.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

        name = view.findViewById(R.id.dialog_name);
        email = view.findViewById(R.id.dialog_email);
        birth = view.findViewById(R.id.dialog_birth);

        profilePhoto = view.findViewById(R.id.dialog_profile_photo);
        shareButton = view.findViewById(R.id.dialog_share);
        callButton = view.findViewById(R.id.dialog_call);

        closeButton = view.findViewById(R.id.dialog_exit_button);
        editButton = view.findViewById(R.id.dialog_edit_button);
        removeButton = view.findViewById(R.id.dialog_remove_button);
        favButton = view.findViewById(R.id.dialog_fav_button);

        phonesLayout = view.findViewById(R.id.linear_layout_phones_parent);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
        //phone.setText(contact.getPhoneNumbers().size()+"");
        //Toast.makeText(getContext(),contact.getPhoneNumbers().toString(),Toast.LENGTH_SHORT).show();
        birth.setText(contact.getBirthday());

        if(contact.isFavorite()){
            favButton.setCircleBackgroundColorResource(R.color.star_color);
        }else{
            favButton.setCircleBackgroundColorResource(R.color.MaterialBlueGrey900);
        }

        if(contact.getPhoneNumbers() !=  null){
            for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
                LinearLayout viewEditText = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_phones, null);
                TextView txt = viewEditText.findViewById(R.id.dialog_phone_title);
                TextView txt2 = viewEditText.findViewById(R.id.dialog_phone_child);
                String phoneN = contact.getPhoneNumber(i);
                String txt_default = getString(R.string.parcial_phone_text) +" " +(i + 1) + ": ";

                if (i >= 1) {
                    txt.setText(txt_default);
                }

                txt2.setText(phoneN);

                phonesLayout.addView(viewEditText);
            }
        }else{
            LinearLayout viewEditText = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_phones, phonesLayout, false);
            TextView txt2 = viewEditText.findViewById(R.id.dialog_phone_child);
            txt2.setText("");
            phonesLayout.addView(viewEditText);
        }

        if (contact.getProfileImage() == null) {
            profilePhoto.setImageResource(R.drawable.ic_person);
            profilePhoto.setBackgroundResource(contact.getColorId());
        } else {
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
                MainActivity.shareContact(getContext(), contact);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogContactShow.this.dismiss();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingContact.removeContact(contact);
                DialogContactShow.this.dismiss();
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
