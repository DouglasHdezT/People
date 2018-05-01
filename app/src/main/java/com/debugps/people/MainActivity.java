package com.debugps.people;

import android.Manifest;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.debugps.people.adapters.ContactsRecentAdapter;
import com.debugps.people.adapters.ContactsDefaultAdapter;
import com.debugps.people.adapters.ContactsFavoritesAdapter;
import com.debugps.people.data.CarryBoy;
import com.debugps.people.data.Contact;
import com.debugps.people.dialogs.DialogContactShow;
import com.debugps.people.fragments.ContactListFragment;
import com.debugps.people.fragments.LandscapeViewFragment;
import com.debugps.people.fragments.MainFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnBindAdapter {

    public static final int ID_DEFAULT_KEY = 1;
    public static final int ID_FAV_KEY = 2;
    public static final int ID_RECENT_KEY = 3;

    public static final String KEY_SAVED_INSTANCE_STATE = "ADustlandFairytale";

    private static Random rn = new Random();

    private ArrayList<Contact> contacts_list = new ArrayList<>();
    private ArrayList<Contact> contactsFav_list = new ArrayList<>();
    private static ArrayList<Contact> contactsRecent_list = new ArrayList<>();

    private CarryBoy carryBoy = new CarryBoy();

    private ContactsDefaultAdapter contactsDefaultAdapter;
    private ContactsFavoritesAdapter contactsFavoritesAdapter;
    private static ContactsRecentAdapter contactsRecentAdapter;

    private FloatingActionButton addContactButton;

    private MainFragment mainFragment = new MainFragment();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addContactButton = findViewById(R.id.floating_button_add_main);

        EnableRuntimePermission();

        if (savedInstanceState != null) {
            carryBoy = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE_STATE);
            contacts_list = carryBoy.getContacts_list();
            contactsFav_list = carryBoy.getContactsFav_list();
            contactsRecent_list = carryBoy.getContactsRecent_list();
        } else {
            addContacts();
        }


        sortList(contacts_list);

        setAdapters();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_list_container_main, mainFragment);
        fragmentTransaction.commit();

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Si te detecto", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(i);
            }
        });

    }

    /*
    Asegurando que se guarden los valores de las listas
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        carryBoy.setContacts_list(contacts_list);
        carryBoy.setContactsFav_list(contactsFav_list);
        carryBoy.setContactsRecent_list(contactsRecent_list);
        outState.putParcelable(KEY_SAVED_INSTANCE_STATE, carryBoy);
        super.onSaveInstanceState(outState);
    }


    /*
    Metodo que implementado para poder setter adpter a los recycler views desde main
     */

    @Override
    public void OnBindAdapter(RecyclerView rv, int id_type_of_fragment, LinearLayoutManager l, GridLayoutManager g) {
        switch (id_type_of_fragment) {

            case ID_DEFAULT_KEY:
                if (isLandscape()) {
                    rv.setLayoutManager(l);
                } else {
                    rv.setLayoutManager(g);
                }
                rv.setAdapter(contactsDefaultAdapter);
                break;

            case ID_FAV_KEY:
                if (isLandscape()) {
                    rv.setLayoutManager(l);
                } else {
                    rv.setLayoutManager(g);
                }
                rv.setAdapter(contactsFavoritesAdapter);
                break;

            case ID_RECENT_KEY:
                rv.setLayoutManager(l);
                rv.setAdapter(contactsRecentAdapter);
                break;
        }
    }


    private void setAdapters() {
        contactsDefaultAdapter = new ContactsDefaultAdapter(contacts_list, isLandscape(), getSupportFragmentManager()) {
            @Override
            public void agregar(int position) {
                int favPosition;
                contactsFav_list.add(contacts_list.get(position));
                favPosition = contactsFav_list.indexOf(contacts_list.get(position));
                contactsFavoritesAdapter.notifyItemInserted(favPosition);
                //Toast.makeText(getApplicationContext(),contacts_list.get(position).getBirthday(), Toast.LENGTH_SHORT).show();
                contactsFavoritesAdapter.notifyDataSetChanged();
                sortList(contactsFav_list);
                contactsFavoritesAdapter.notifyItemRangeChanged(0, contactsFav_list.size());
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

        contactsFavoritesAdapter = new ContactsFavoritesAdapter(contactsFav_list, isLandscape(), getSupportFragmentManager()) {
            @Override
            public void remover(int position) {
                Contact cFav = contactsFav_list.get(position);
                int posD = contacts_list.indexOf(cFav);

                contacts_list.get(posD).setFavorite(false);
                contactsDefaultAdapter.notifyItemChanged(posD);

                contactsFav_list.remove(position);
                contactsFavoritesAdapter.notifyItemRemoved(position);
                contactsFavoritesAdapter.notifyItemRangeChanged(position, contactsFav_list.size());

            }
        };

        contactsRecentAdapter = new ContactsRecentAdapter(contactsRecent_list, getSupportFragmentManager(), this, isLandscape());

    }

    public static void showContactPotrait(Contact contact, FragmentManager fragmentManager) {
        if (contact != null) {
            DialogContactShow dialogContactShow = DialogContactShow.newInstance(contact);
            dialogContactShow.show(fragmentManager, "dialog");
        }
    }

    public static void showContactLandscape(Contact contact, FragmentManager fragmentManager) {
        if (contact != null) {
            LandscapeViewFragment landscapeViewFragment = LandscapeViewFragment.newInstance(contact);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contact_container_main, landscapeViewFragment);
            fragmentTransaction.commit();
        }


    }

    public static void callContact(Context context, Contact contact) {
        if (contact.getPhoneNumbers() == null){
            return;
        }

        addContactToRecent(contact);

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse(("tel:" + contact.getPhoneNumbers())));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(i);
    }

    private static void addContactToRecent(Contact contact){
        int index =0;
        if(contactsRecent_list.contains(contact)){
            index = contactsRecent_list.indexOf(contact);
            contactsRecent_list.remove(index);
            contactsRecentAdapter.notifyItemRemoved(index);
            contactsRecentAdapter.notifyItemRangeChanged(index, contactsRecent_list.size());
        }
        contact.upCantCalls();
        contactsRecent_list.add(0, contact);
        contactsRecentAdapter.notifyItemInserted(0);
        contactsRecentAdapter.notifyItemRangeChanged(0, contactsRecent_list.size());
    }

    public static void shareContact(Context context, Contact contact){

        Intent i = new Intent(Intent.ACTION_SEND);

        String text="Contacto: "+ contact.getName() + "\nTelefono: "+contact.getPhoneNumbers();
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.setType("text/plain");

        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.intent_title)));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }

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
        String bDay = null;
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

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String named = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Cursor bdc = contentResolver.query(android.provider.ContactsContract.Data.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Event.DATA }, android.provider.ContactsContract.Data.CONTACT_ID+" = "+id+" AND "+ ContactsContract.Data.MIMETYPE+" = '"+ ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE+"' AND "+ ContactsContract.CommonDataKinds.Event.TYPE+" = "+ ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, null, android.provider.ContactsContract.Data.DISPLAY_NAME);
                    if (bdc.getCount() > 0) {
                        while (bdc.moveToNext()) {
                            String birthday = bdc.getString(0);
                            // now "id" is the user's unique ID, "name" is his full name and "birthday" is the date and time of his birth
                            contact.setBirthday(birthday);
                        }
                    }
                    bdc.close();

                    contact.setColorId(getColorId());

                    contacts_list.add(contact);

                }
            }
        }
    }

    private static int getColorId(){
        //Random rn2 = new Random();
        int rnNumber = Math.abs((rn.nextInt() % 17)) + 1;
        int idColor=R.color.MaterialDeepPurple900;

        switch (rnNumber){
            case 1:
                idColor = R.color.MaterialBlue900;
                break;
            case 2:
                idColor = R.color.MaterialRed900;
                break;
            case 3:
                idColor = R.color.MaterialPurple900;
                break;
            case 4:
                idColor = R.color.MaterialBlueGrey900;
                break;
            case 5:
                idColor = R.color.MaterialCyan900;
                break;
            case 6:
                idColor = R.color.MaterialIndigo900;
                break;
            case 7:
                idColor = R.color.MaterialLime900;
                break;
            case 8:
                idColor = R.color.MaterialBrown900;
                break;
            case 9:
                idColor = R.color.MaterialOrange900;
                break;
            case 10:
                idColor = R.color.MaterialPink900;
                break;
            case 11:
                idColor = R.color.MaterialTeal900;
                break;
            case 12:
                idColor = R.color.MaterialLightGreen900;
                break;
            case 13:
                idColor = R.color.MaterialLightBlue900;
                break;
            case 14:
                idColor = R.color.MaterialDeepPurple900;
                break;
            case 15:
                idColor = R.color.MaterialDeepOrange900;
                break;
            case 16:
                idColor = R.color.MaterialGrey900;
                break;
            case 17:
                idColor = R.color.MaterialGreen900;
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

    public static void sortList(ArrayList<Contact> list){
        Collections.sort(list, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

}