package com.debugps.people;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.debugps.people.data.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactActivity extends AppCompatActivity {

    private static final String KEY_STRING = "PequenoAngelOscuro";
    private static final String KEY_URI = "SaveMe";
    private static final int RESULT_LOAD_IMG = 999;


    private ImageView profilePhoto;
    private ImageButton refresh;
    private CircleImageView putNewPhone;
    private CircleImageView putImageButton;

    private EditText name;
    private EditText email;
    private EditText principalPhone;

    private Button birthday;

    private LinearLayout phonesLayout;

    private String birthText = "";
    private Uri image_uri;

    private boolean flag = false;

    Contact contact;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        profilePhoto = findViewById(R.id.layout_edit_profile_photo);
        refresh =  findViewById(R.id.layout_edit_button);
        putNewPhone = findViewById(R.id.layout_edit_phone_button);

        name =  findViewById(R.id.layout_edit_name);
        principalPhone = findViewById(R.id.layout_edit_phone);
        email = findViewById(R.id.layout_edit_email);

        birthday = findViewById(R.id.layout_edit_birthday);

        phonesLayout =  findViewById(R.id.linear_layout_edit_contact_phones);
        putImageButton = findViewById(R.id.layout_edit_put_image_button);

        intent = getIntent();
        contact = intent.getParcelableExtra(MainActivity.KEY_EDIT_CONTACT);

        if(contact.getPhoneNumbers().size() >1){
            for(int i=1; i<contact.getPhoneNumbers().size(); i++){
                final LinearLayout viewNewPhone = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_add_phone, null);
                CircleImageView removeButton =  viewNewPhone.findViewById(R.id.layout_remove_phone_button);
                EditText editTxt= viewNewPhone.findViewById(R.id.layout_add_phone_new);

                editTxt.setText(contact.getPhoneNumber(i));
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phonesLayout.removeView(viewNewPhone);
                    }});
                phonesLayout.addView(viewNewPhone);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final int contactColor = MainActivity.getColorId();
        int buttonColor;

        do{
            buttonColor = MainActivity.getColorId();
        }while(buttonColor == contactColor);

        putImageButton.setCircleBackgroundColorResource(buttonColor);
        profilePhoto.setBackgroundResource(contactColor);
        refresh.setBackgroundResource(buttonColor);


        if(name.getText().toString().equals("")){
            name.setText(contact.getName());
        }

        if(email.getText().toString().equals("")){
            name.setText(contact.getEmail());
        }

        if(birthText.equals("")){
            birthday.setText(contact.getBirthday());
        }else{
            birthday.setText(birthText);
        }

        if(image_uri != null){
            profilePhoto.setImageURI(image_uri);
        }else{
            if (contact.getProfileImage() != null || !flag){
                profilePhoto.setImageURI(contact.getProfileImage());
            }else{
                profilePhoto.setImageResource(R.drawable.ic_person);
            }
        }

        principalPhone.setText(contact.getPhoneNumbers().size()==0 ?"":contact.getPhoneNumber(0));



        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthText = year+"-"+month+"-"+dayOfMonth;
                birthday.setText(birthText);
            }
        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditContactActivity.this, 0, listener, 1992,1,1);
                datePickerDialog.show();
            }
        });

        putImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecuredGetImage();
            }
        });

        putNewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout viewNewPhone = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_add_phone, null);
                CircleImageView removeButton =  viewNewPhone.findViewById(R.id.layout_remove_phone_button);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phonesLayout.removeView(viewNewPhone);
                    }});
                phonesLayout.addView(viewNewPhone);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.getPhoneNumbers().clear();
                contact.setPhoneNumber(principalPhone.getText().toString());
                contact.setName(name.getText().toString());
                contact.setBirthday(birthText);
                contact.setEmail(email.getText().toString());

                for(int i = 0; i< phonesLayout.getChildCount(); i++){
                    View view = phonesLayout.getChildAt(i);
                    EditText phoneAct = view.findViewById(R.id.layout_add_phone_new);
                    contact.setPhoneNumber(phoneAct.getText().toString());
                }

                contact.setProfileImage(image_uri);

                Intent intent2 = new Intent();
                intent2.putExtra(MainActivity.KEY_EDIT_CONTACT, contact);
                intent2.putExtra(MainActivity.KEY_INT_POSITION, intent.getIntExtra(MainActivity.KEY_INT_POSITION, 1));
                EditContactActivity.this.setResult(RESULT_OK, intent2);

                finish();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_STRING, birthText);
        outState.putParcelable(KEY_URI, image_uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        birthText = savedInstanceState.getString(KEY_STRING);
        image_uri = savedInstanceState.getParcelable(KEY_URI);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode == RESULT_LOAD_IMG){
            if (resultCode == RESULT_OK) {
                image_uri = data.getData();
                profilePhoto.setImageURI(image_uri);
            }else {
                Toast.makeText(EditContactActivity.this, R.string.error_get_image_2, Toast.LENGTH_LONG).show();
                profilePhoto.setImageResource(R.drawable.ic_person);
                image_uri = null;
                flag = true;
            }
        }
    }

    public void SecuredGetImage(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RESULT_LOAD_IMG);
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            EditContactActivity.this.startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RESULT_LOAD_IMG){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SecuredGetImage();
            }else{
                Toast.makeText(this, getString(R.string.intent_read_image), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
