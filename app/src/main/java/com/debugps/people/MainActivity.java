package com.debugps.people;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.debugps.people.adapters.ContactsDefaultAdapter;
import com.debugps.people.adapters.ContactsFavoritesAdapter;
import com.debugps.people.adapters.ContactsLandscapeAdapter;
import com.debugps.people.data.Contact;
import com.debugps.people.fragments.ContactListFragment;
import com.debugps.people.fragments.MainFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnBindAdapter {

    public static final int ID_DEFAULT_KEY = 1;
    public static final int ID_FAV_KEY = 2;
    public static final int ID_RECENT_KEY = 3;

    public static final String KEY_SAVED_INSTANCE_STATE = "ADustlandFairytale";
    public static final String KEY_SAVED_INSTANCE_STATE_FAV = "ShotMeAtTheNight";

    private ArrayList<Contact> contacts_list = new ArrayList<>();
    private ArrayList<Contact> contactsFav_list = new ArrayList<>();
    private ArrayList<Contact> contactsRecent_list = new ArrayList<>();

    private ContactsDefaultAdapter contactsDefaultAdapter;
    private ContactsFavoritesAdapter contactsFavoritesAdapter;

    private ContactsLandscapeAdapter contactsLandscapeAdapter_default;

    private MainFragment mainFragment = new MainFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnableRuntimePermission();

        if(savedInstanceState != null){
            contacts_list= (ArrayList<Contact>) savedInstanceState.getSerializable(KEY_SAVED_INSTANCE_STATE);
            contactsFav_list= (ArrayList<Contact>) savedInstanceState.getSerializable(KEY_SAVED_INSTANCE_STATE_FAV);
        }else{
            addContacts();
        }

        sortList(contacts_list);

        setAdapters();

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_list_container_main, mainFragment);

        fragmentTransaction.commit();

    }

    /*
    Asegurando que se guarden los valores de las listas
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_SAVED_INSTANCE_STATE, contacts_list);
        outState.putSerializable(KEY_SAVED_INSTANCE_STATE_FAV, contactsFav_list);
        super.onSaveInstanceState(outState);
    }

    /*
    Metodo que implementado para poder setter adpter a los recycler views desde main
     */

    @Override
    public void OnBindAdapter(RecyclerView rv, int id_type_of_fragment, LinearLayoutManager l, GridLayoutManager g) {
        switch(id_type_of_fragment){

            case ID_DEFAULT_KEY:
                if(isLandscape()){
                    rv.setLayoutManager(l);
                }else{
                    rv.setLayoutManager(g);
                }
                rv.setAdapter(contactsDefaultAdapter);
                break;

            case ID_FAV_KEY:
                if(isLandscape()){
                    rv.setLayoutManager(l);
                }else{
                    rv.setLayoutManager(g);
                }
                rv.setAdapter(contactsFavoritesAdapter);
                break;

            case ID_RECENT_KEY:
                break;
        }
    }


    private void setAdapters(){
        contactsDefaultAdapter = new ContactsDefaultAdapter(contacts_list, isLandscape()) {
            @Override
            public void agregar(int position) {
                int favPosition;
                contactsFav_list.add(contacts_list.get(position));
                favPosition = contactsFav_list.indexOf(contacts_list.get(position));
                contactsFavoritesAdapter.notifyItemInserted(favPosition);
                Toast.makeText(getApplicationContext(),"Llegue", Toast.LENGTH_SHORT).show();
                contactsFavoritesAdapter.notifyDataSetChanged();
                sortList(contactsFav_list);
                contactsFavoritesAdapter.notifyItemRangeChanged(0,contactsFav_list.size());
            }

            @Override
            public void remover(int position) {
                Contact c = contacts_list.get(position);
                int favPosition = contactsFav_list.indexOf(c);
                contactsFav_list.remove(favPosition);
                contactsFavoritesAdapter.notifyItemRemoved(favPosition);
                contactsFavoritesAdapter.notifyItemRangeChanged(favPosition, contactsFav_list.size());
                notifyDataSetChanged();
            }
        };

        contactsFavoritesAdapter = new ContactsFavoritesAdapter(contactsFav_list) {
            @Override
            public void agregar(int position) {

            }

            @Override
            public void remover(int position) {

            }
        };

        contactsLandscapeAdapter_default = new ContactsLandscapeAdapter(contacts_list);
    }

    /*
    Inflando el boton de busqueda en la ActionBar...
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView s = (SearchView) menu.findItem(R.id.search_main).getActionView();

        //Listener del search bar...

        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
    }

    public void addContacts(){
        Contact contact;
        String phoneNumber = null;
        String email = null;
        String image_uri;
        Bitmap bitmap=null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri phoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.DATA;

        Uri emailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String emailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;


        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI,null,null,null,null);

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                contact = new Contact();
                String contactID = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                image_uri = cursor
                        .getString(cursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media .getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        contact.setProfileImage(bitmap);
                    }catch (FileNotFoundException e) {
                        e.printStackTrace(); }
                    catch (IOException e) {
                        e.printStackTrace(); }
                }

                if(hasPhoneNumber >0){
                    contact.setName(name);
                    Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI,null,Phone_CONTACT_ID+" =? ", new String[]{contactID},null);

                    while (phoneCursor.moveToNext()){
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setPhoneNumbers(phoneNumber);
                    }
                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(emailCONTENT_URI,null,emailCONTACT_ID+" =? ",new String[]{contactID},null);

                    while (emailCursor.moveToNext()){
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        contact.setEmail(email);
                    }
                    emailCursor.close();

                    contact.setColorId(getColorId(contact.getName().toUpperCase().charAt(0)));

                    contacts_list.add(contact);

                }
            }
        }
    }

    private static int getColorId(char letra){
        int idColor=R.color.MaterialDeepPurple900;
        switch (letra){
            case 'A':
                idColor = R.color.MaterialBlue900;
                break;
            case 'B':
                idColor = R.color.MaterialRed900;
                break;
            case 'C':
                idColor = R.color.MaterialPurple900;
                break;
            case 'D':
                idColor = R.color.MaterialBlueGrey900;
                break;
            case 'E':
                idColor = R.color.MaterialCyan900;
                break;
            case 'F':
                idColor = R.color.MaterialIndigo900;
                break;
            case 'G':
                idColor = R.color.MaterialLime900;
                break;
            case 'H':
                idColor = R.color.MaterialBrown900;
                break;
            case 'I':
                idColor = R.color.MaterialOrange900;
                break;
            case 'J':
                idColor = R.color.MaterialPink900;
                break;
            case 'K':
                idColor = R.color.MaterialTeal900;
                break;
            case 'L':
                idColor = R.color.MaterialLightGreen900;
                break;
            case 'M':
                idColor = R.color.MaterialLightBlue900;
                break;
            case 'N':
                idColor = R.color.MaterialGreen900;
                break;
            case 'Ñ':
                idColor = R.color.MaterialBlueGrey900;
                break;
            case 'O':
                idColor = R.color.MaterialGrey900;
                break;
            case 'P':
                idColor = R.color.MaterialBlue900;
                break;
            case 'Q':
                idColor = R.color.MaterialRed900;
                break;
            case 'R':
                idColor = R.color.MaterialPink900;
                break;
            case 'S':
                idColor = R.color.MaterialPurple900;
                break;
            case 'T':
                idColor = R.color.MaterialOrange900;
                break;
            case 'U':
                idColor = R.color.MaterialCyan900;
                break;
            case 'V':
                idColor = R.color.MaterialTeal900;
                break;
            case 'W':
                idColor = R.color.MaterialIndigo900;
                break;
            case 'X':
                idColor = R.color.MaterialDeepOrange900;
                break;
            case 'Y':
                idColor = R.color.MaterialLightBlue900;
                break;
            case 'Z':
                idColor = R.color.MaterialLime900;
                break;
        }

        return idColor;
    }

    public void EnableRuntimePermission(){

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        123);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public boolean isLandscape(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return false;
        }else{
            return true;
        }
    }

    public static ArrayList<Contact> sortList(ArrayList<Contact> list){
        Collections.sort(list, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return list;
    }


}

