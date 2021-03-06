package com.debugps.people;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.debugps.people.data.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {

    private static final String KEY_STRING_BIRTH = "Stylo";
    private static final int RESULT_LOAD_IMG = 1223;

    public static final String KEY_URI = "Spaceman";
    public static final String KEY_CONTACT = "PrizeFighter";
    private static final String KEY_ARRAY = "MagoDeOz";

    private ImageView profilePhoto;
    private CircleImageView putImageButton;
    private CircleImageView putNewPhone;
    private ImageButton addButton;

    private LinearLayout phonesLayout;

    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private Button birthInput;

    private ArrayList<String> buff_phones;

    private Uri image_uri;

    String birhtday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        if(savedInstanceState != null){
            buff_phones = savedInstanceState.getStringArrayList(KEY_ARRAY);
        }else {
            buff_phones= null;
        }

        profilePhoto = findViewById(R.id.layout_add_profile_photo);
        putImageButton = findViewById(R.id.layout_add_put_image_button);
        putNewPhone = findViewById(R.id.layout_add_phone_button);
        addButton =  findViewById(R.id.layout_add_button);

        nameInput  = findViewById(R.id.layout_add_name);
        phoneInput = findViewById(R.id.layout_add_phone);
        emailInput = findViewById(R.id.layout_add_email);
        birthInput = findViewById(R.id.layout_add_birthday);

        phonesLayout =  findViewById(R.id.linear_layout_new_contact_phones);

        if(buff_phones != null){
            //Log.d("MSMSMSMSM", buff_phones.toString());
            for(int i=0; i<buff_phones.size(); i++){
                final LinearLayout viewNewPhone = (LinearLayout) getLayoutInflater().inflate(R.layout.linear_layout_add_phone, null);
                CircleImageView removeButton =  viewNewPhone.findViewById(R.id.layout_remove_phone_button);
                EditText editTxt= viewNewPhone.findViewById(R.id.layout_add_phone_new);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phonesLayout.removeView(viewNewPhone);
                    }});
                phonesLayout.addView(viewNewPhone);
            }
        }else{
            buff_phones = new ArrayList<>();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final int colorContact =MainActivity.getColorId();
        int colorButtons = R.color.MaterialBlueGrey900;

        if(buff_phones != null){
            if(buff_phones.size() !=0){
                for(int i = 0; i<phonesLayout.getChildCount();i++){
                    EditText edt = phonesLayout.getChildAt(i).findViewById(R.id.layout_add_phone_new);
                    edt.setText(buff_phones.get(i));
                }
            }
        }

        if(birhtday == null){
            birthInput.setHint(R.string.edit_text_birth);
        }else{
            birthInput.setText(birhtday);
        }

        if(image_uri == null){
            profilePhoto.setImageResource(R.drawable.ic_person);
        }else{
            profilePhoto.setImageURI(image_uri);
        }

        do{
            colorButtons = MainActivity.getColorId();
        }while(colorButtons == colorContact);

        putImageButton.setCircleBackgroundColorResource(colorButtons);
        addButton.setBackgroundResource(colorButtons);

        profilePhoto.setBackgroundResource(colorContact);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birhtday = year+"-"+month+"-"+dayOfMonth;
                birthInput.setText(birhtday);
            }
        };

        birthInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddContactActivity.this, 0, listener, 1992,1,1);
                datePickerDialog.show();
            }
        });

        putImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecuredGetImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput.getText().toString().equals("")){
                    Toast.makeText(AddContactActivity.this, R.string.error_add_contact, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                Contact contact = new Contact();

                contact.setName(nameInput.getText().toString());
                contact.setBirthday(birthInput.getText().toString());
                contact.setPhoneNumber(phoneInput.getText().toString());
                contact.setEmail(emailInput.getText().toString());
                contact.setColorId(colorContact);
                contact.setProfileImage(image_uri);

                for(int i = 0; i< phonesLayout.getChildCount(); i++){
                    View view = phonesLayout.getChildAt(i);
                    EditText phoneAct = view.findViewById(R.id.layout_add_phone_new);
                    contact.setPhoneNumber(phoneAct.getText().toString());
                }

                intent.putExtra(KEY_CONTACT, contact);
                setResult(RESULT_OK, intent);

                finish();
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        buff_phones.clear();
        for(int i = 0; i< phonesLayout.getChildCount(); i++){
            View view = phonesLayout.getChildAt(i);
            EditText phoneAct = view.findViewById(R.id.layout_add_phone_new);
            //Log.d("MSM", phoneAct.getText().toString());
            buff_phones.add(phoneAct.getText().toString());
        }
        outState.putStringArrayList(KEY_ARRAY, buff_phones);
        outState.putString(KEY_STRING_BIRTH, birhtday);
        outState.putParcelable(KEY_URI, image_uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        birhtday = savedInstanceState.getString(KEY_STRING_BIRTH);
        image_uri = savedInstanceState.getParcelable(KEY_URI);
        buff_phones =  savedInstanceState.getStringArrayList(KEY_ARRAY);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode == RESULT_LOAD_IMG){
            if (resultCode == RESULT_OK) {
                image_uri = data.getData();
                profilePhoto.setImageURI(image_uri);
            }else {
                Toast.makeText(AddContactActivity.this, R.string.error_get_image_2, Toast.LENGTH_LONG).show();
                profilePhoto.setImageResource(R.drawable.ic_person);
                image_uri = null;
            }
        }
    }

    public void SecuredGetImage(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RESULT_LOAD_IMG);
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            AddContactActivity.this.startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
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
