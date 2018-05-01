package com.debugps.people;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {

    private static final String KEY_STRING_BIRTH = "Stylo";
    private static final int RESULT_LOAD_IMG = 1223;

    private ImageView profilePhoto;
    private CircleImageView putImageButton;
    private ImageButton addButton;

    private EditText nameInput;
    private EditText phoneInput;
    private EditText emailInput;
    private Button birthInput;

    String birhtday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        profilePhoto = findViewById(R.id.layout_add_profile_photo);
        putImageButton = findViewById(R.id.layout_add_put_image_button);
        addButton =  findViewById(R.id.layout_add_button);

        nameInput  = findViewById(R.id.layout_add_name);
        phoneInput = findViewById(R.id.layout_add_phone);
        emailInput = findViewById(R.id.layout_add_email);
        birthInput = findViewById(R.id.layout_add_birthday);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int colorContact, colorButtons;

        if(birhtday == null){
            birthInput.setHint(R.string.edit_text_birth);
        }else{
            birthInput.setText(birhtday);
        }

        colorContact = MainActivity.getColorId();

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

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_STRING_BIRTH, birhtday);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        birhtday = savedInstanceState.getString(KEY_STRING_BIRTH);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePhoto.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddContactActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddContactActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            profilePhoto.setImageResource(R.drawable.ic_person);
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
