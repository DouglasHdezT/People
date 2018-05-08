package com.debugps.people;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.debugps.people.intefaces.OnSettingContact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static com.debugps.people.AddContactActivity.KEY_CONTACT;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnBindAdapter, OnSettingContact {

    public static final int ID_DEFAULT_KEY = 1;
    public static final int ID_FAV_KEY = 2;
    public static final int ID_RECENT_KEY = 3;

    public static  final int READ_CONTACTS_KEY= 123;
    private static final int CALL_PHONE_KEY = 666;
    private static final int GET_NEW_CONTACT_KEY = 127;
    private static final int EDIT_CONTACT_CODE = 2002;

    private boolean inSearch = false;

    public static final String KEY_SAVED_INSTANCE_STATE = "ADustlandFairytale";
    public static final String KEY_EDIT_CONTACT = "RockTheHouse";
    public static final String KEY_INT_POSITION = "PasenYBeban";

    private static Random rn = new Random();

    private static CarryBoy carryBoy = new CarryBoy();

    protected static ArrayList<Contact> contacts_list = new ArrayList<>();
    private static ArrayList<Contact> contactsFav_list = new ArrayList<>();
    private static ArrayList<Contact> contactsRecent_list = new ArrayList<>();

    protected static ArrayList<Contact> contacts_list_query = new ArrayList<>();

    private ContactsDefaultAdapter contactsDefaultAdapter;
    private ContactsFavoritesAdapter contactsFavoritesAdapter;
    private ContactsRecentAdapter contactsRecentAdapter;

    private FloatingActionButton addContactButton;

    private MainFragment mainFragment = new MainFragment();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addContactButton = findViewById(R.id.floating_button_add_main);

        if (savedInstanceState != null) {
            carryBoy = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE_STATE);
            if (carryBoy != null) {
                contacts_list = carryBoy.getContacts_list();
                contactsFav_list = carryBoy.getContactsFav_list();
                contactsRecent_list = carryBoy.getContactsRecent_list();
                contacts_list_query = carryBoy.getContacts_list_query();
            }
        }

        setAdapters();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_list_container_main, mainFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(contacts_list.size() == 0){
            SecuredAddContacts();
        }

        contactsDefaultAdapter.notifyItemRangeChanged(0, contacts_list.size());

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddContactActivity.class);
                startActivityForResult(i, GET_NEW_CONTACT_KEY);
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
        carryBoy.setContacts_list_query(contacts_list_query);
        outState.putParcelable(KEY_SAVED_INSTANCE_STATE, carryBoy);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        carryBoy = savedInstanceState.getParcelable(KEY_SAVED_INSTANCE_STATE);
        if (carryBoy != null) {
            contacts_list = carryBoy.getContacts_list();
            contactsFav_list = carryBoy.getContactsFav_list();
            contactsRecent_list = carryBoy.getContactsRecent_list();
            contacts_list_query = carryBoy.getContacts_list_query();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_NEW_CONTACT_KEY){
            if(resultCode ==  RESULT_OK){
                Contact contact = data.getParcelableExtra(AddContactActivity.KEY_CONTACT);
                contacts_list.add(contact);
                contactsDefaultAdapter.notifyItemInserted(contacts_list.size() - 1);
                Collections.sort(contacts_list);
                contactsDefaultAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == EDIT_CONTACT_CODE){
            if(resultCode == RESULT_OK){
                int defPosition =data.getIntExtra(KEY_INT_POSITION, 0);
                Contact contact1 = data.getParcelableExtra(MainActivity.KEY_EDIT_CONTACT);
                Contact contact = contacts_list.get(defPosition);

                int indexFav= contactsFav_list.indexOf(contact);
                int indexRec= contactsRecent_list.indexOf(contact);
                int indexQuery =  contacts_list_query.indexOf(contact);

                Log.d("MSM", contact.getName());

                contacts_list.set(defPosition, contact1);

                if(indexFav >=0){
                    contactsFav_list.set(indexFav, contact1);
                    contactsFavoritesAdapter.notifyDataSetChanged();
                }

                if(indexRec >=0){
                    contactsRecent_list.set(indexRec, contact1);
                    contactsRecentAdapter.notifyDataSetChanged();
                }

                if(indexQuery>=0){
                    contacts_list_query.set(indexQuery, contact1);
                }
                contactsDefaultAdapter.notifyDataSetChanged();

                if(isLandscape()){
                    LandscapeViewFragment landscapeViewFragment = LandscapeViewFragment.newInstance(contact1);
                    FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contact_container_main, landscapeViewFragment);
                    ft.commit();
                }else{
                    DialogContactShow dialogContactShow = DialogContactShow.newInstance(contact1);
                    dialogContactShow.show(getSupportFragmentManager(), "dialog");
                }
            }
        }
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
        contactsDefaultAdapter = new ContactsDefaultAdapter(contacts_list, isLandscape(), getSupportFragmentManager(), MainActivity.this) {
            @Override
            public void agregar(int position) {
                int favPosition;
                Contact c;

                if(inSearch){
                    c= contacts_list_query.get(position);
                    contactsFav_list.add(contacts_list_query.get(position));
                    favPosition = contactsFav_list.indexOf(c);
                }else{
                    c = contacts_list.get(position);
                    contactsFav_list.add(contacts_list.get(position));
                    favPosition = contactsFav_list.indexOf(c);
                }

                contactsFavoritesAdapter.notifyItemInserted(favPosition);
                contactsFavoritesAdapter.notifyDataSetChanged();
                sortList(contactsFav_list);
                contactsFavoritesAdapter.notifyItemRangeChanged(0, contactsFav_list.size());

                if(isLandscape()){
                    showContactLandscape(c, getSupportFragmentManager());
                }

            }

            @Override
            public void remover(int position) {
                Contact c;
                if(inSearch){
                    c = contacts_list_query.get(position);
                }else{
                    c = contacts_list.get(position);
                }

                int favPosition = contactsFav_list.indexOf(c);
                contactsFav_list.remove(favPosition);
                contactsFavoritesAdapter.notifyItemRemoved(favPosition);
                contactsFavoritesAdapter.notifyItemRangeChanged(favPosition, contactsFav_list.size());
                notifyDataSetChanged();

                if(isLandscape()){
                    showContactLandscape(c, getSupportFragmentManager());
                }
            }
        };

        contactsFavoritesAdapter = new ContactsFavoritesAdapter(contactsFav_list, isLandscape(), getSupportFragmentManager()) {
            @Override
            public void remover(int position) {
                Contact cFav = contactsFav_list.get(position);
                int posD;
                Contact c;

                posD = contacts_list.indexOf(cFav);
                contacts_list.get(posD).setFavorite(false);
                c = contacts_list.get(posD);

                contactsDefaultAdapter.notifyItemChanged(posD);

                contactsFav_list.remove(position);
                contactsFavoritesAdapter.notifyItemRemoved(position);
                contactsFavoritesAdapter.notifyItemRangeChanged(position, contactsFav_list.size());

                if(isLandscape()){
                    showContactLandscape(c, getSupportFragmentManager());
                }
            }
        };

        contactsRecentAdapter = new ContactsRecentAdapter(contactsRecent_list, getSupportFragmentManager(), MainActivity.this, isLandscape());

    }

    /*
    Metodos estaticos para mostrar la informacion del contacto tanto en Landscape como en Potrait
     */

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

    /*
    Metodo para compartir la informacion del contacto
     */

    public static void shareContact(Context context, Contact contact){

        Intent i = new Intent(Intent.ACTION_SEND);
        String text=context.getString(R.string.contact_share_text)+"\n" +context.getString(R.string.name_share_text)+ contact.getName();

        if(contact.getPhoneNumbers() != null){
            if(contact.getPhoneNumbers().size()>0){
                for (int j = 0; j< contact.getPhoneNumbers().size(); j++){
                    text = text + "\n"+ context.getString(R.string.phone_share_text)+(j+1)+": "+contact.getPhoneNumber(j);
                }
            }
        }

        if(!contact.getEmail().equals("")){
            text =  text + "\n" + context.getString(R.string.email_share_text)+ contact.getEmail();
        }

        if(!contact.getBirthday().equals("")){
            text =  text + "\n" + context.getString(R.string.birthday_share_text) + contact.getBirthday();
        }

        i.putExtra(Intent.EXTRA_TEXT, text);

        if(contact.getProfileImage() != null){
            i.putExtra(Intent.EXTRA_STREAM, contact.getProfileImage());
            i.setType("*/*");
        }else{
            i.setType("text/plain");
        }

        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.intent_title)));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }

    }

    /*
    Inflando el boton de busqueda en la ActionBar...
    Ademas metodo para filtar cualquier arreglo con una query
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
                if(newText.equals("")){
                    contactsDefaultAdapter.chargeFilter(contacts_list);
                    inSearch = false;
                    return true;
                }

                inSearch = true;
                contacts_list_query = filterData(contacts_list, newText);
                contactsDefaultAdapter.chargeFilter(contacts_list_query);

                return true;
            }
        });

        return true;
    }

    private ArrayList<Contact> filterData(ArrayList<Contact> contacts, String query){
        ArrayList<Contact> filtered = new ArrayList<>();
        query = query.toUpperCase();

        for(Contact model : contacts){
            final String textName = model.getName().toUpperCase();
            final String textEmail = model.getEmail().toUpperCase();
            final String textBirth = model.getBirthday().toUpperCase();
            if(textName.startsWith(query) || textName.contains(query) || textEmail.startsWith(query) || textEmail.contains(query)
                    || textBirth.startsWith(query) || textBirth.contains(query)){
                filtered.add(model);
            }

            if(model.getPhoneNumbers() != null){
                for(int i= 0; i<model.getPhoneNumbers().size(); i++){
                    if((model.getPhoneNumber(i).startsWith(query) || model.getPhoneNumber(i).contains(query)) && !filtered.contains(model)){
                        filtered.add(model);
                    }
                }
            }
        }

        return filtered;
    }

    /*
    Metodo para añadir los contactos del telefono cuando se inicia la app
     */
    public void addContacts(){
        contacts_list.clear();
        Contact contact;
        String phoneNumber = null;
        String email = null;
        String image_uri;

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
                if(image_uri != null){
                    contact.setProfileImage(Uri.parse(image_uri));
                }

                if(hasPhoneNumber >0){
                    contact.setName(name);
                    Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI,null,Phone_CONTACT_ID+" =? ", new String[]{contactID},null);

                    phoneCursor.moveToFirst();
                    while (!phoneCursor.isAfterLast()){
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setPhoneNumber(phoneNumber);
                        phoneCursor.moveToNext();
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
                    contactsDefaultAdapter.notifyDataSetChanged();
                    Collections.sort(contacts_list);
                    contactsDefaultAdapter.notifyItemRangeChanged(0,contacts_list.size());

                }
            }
        }
    }

    public void SecuredAddContacts(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) !=  PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_KEY);
        }else{
            addContacts();
            contactsDefaultAdapter.notifyItemRangeChanged(0, contacts_list.size());
        }
    }

    /*
    * Metodo que escucha repuestas a intents que han sido llamados
    * */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_CONTACTS_KEY){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SecuredAddContacts();
            }else{
                Toast.makeText(this, getString(R.string.read_contact_permission), Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == CALL_PHONE_KEY){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.call_intent_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    Paleta Material 900 aleatoria
     */
    public static int getColorId(){
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

    public static void sortList(ArrayList<Contact> list){
        Collections.sort(list, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /*
    Metodos de la interfaz OnSettingContact
     */
    @Override
    public void setFavorited(Contact contact) {
        contact.setFavorite(true);
        contactsFav_list.add(contact);
        contactsFavoritesAdapter.notifyItemInserted(contactsFav_list.indexOf(contact));
        contactsDefaultAdapter.notifyDataSetChanged();
        Collections.sort(contactsFav_list);
        contactsFavoritesAdapter.notifyDataSetChanged();
    }

    @Override
    public void unsetFavorited(Contact contact) {
        contact.setFavorite(false);
        int index =  contactsFav_list.indexOf(contact);
        contactsFav_list.remove(contact);
        contactsFavoritesAdapter.notifyItemRemoved(index);
        contactsFavoritesAdapter.notifyItemRangeChanged(index, contactsFav_list.size());
        contactsDefaultAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeContact(Contact contact) {
        int position  = contacts_list.indexOf(contact);
        int positionFav = contactsFav_list.indexOf(contact);
        int positionQuery = contacts_list_query.indexOf(contact);
        int positionRec = contactsRecent_list.indexOf(contact);

        if(positionFav >= 0){
            contactsFav_list.remove(positionFav);
            contactsFavoritesAdapter.notifyItemRemoved(positionFav);
            contactsFavoritesAdapter.notifyItemRangeChanged(positionFav, contactsFav_list.size());
        }

        if(positionRec >= 0){
            contactsRecent_list.remove(positionRec);
            contactsRecentAdapter.notifyItemRemoved(positionRec);
            contactsRecentAdapter.notifyItemRangeChanged(positionRec, contactsRecent_list.size());
        }

        contacts_list.remove(position);
        if(positionQuery>=0){
            contacts_list_query.remove(positionQuery);
            contactsDefaultAdapter.notifyItemRemoved(positionQuery);
            contactsDefaultAdapter.notifyItemRangeChanged(positionQuery, contacts_list_query.size());
        }else{
            contactsDefaultAdapter.notifyItemRemoved(position);
            contactsDefaultAdapter.notifyItemRangeChanged(position, contacts_list.size());
        }

    }

    @Override
    public void editContact(Contact contact) {
        Intent i = new Intent(MainActivity.this, EditContactActivity.class);
        i.putExtra(KEY_EDIT_CONTACT, contact);
        i.putExtra(KEY_INT_POSITION, contacts_list.indexOf(contact));
        startActivityForResult(i, EDIT_CONTACT_CODE);
    }


    //Metodos para realizar llamadas.
    @Override
    public void callContact(final Contact contact) {
        if (contact.getPhoneNumbers() == null || contact.getPhoneNumbers().size() == 0){
            return;
        }else if(contact.getPhoneNumbers().size() == 1){
            addContactToRecent(contact, contact.getPhoneNumber(0));
            perfomCall(contact.getPhoneNumber(0));
        }else{

            final CharSequence[] items = contact.getArrayOfPhones();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.ask_call_dialog);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    addContactToRecent(contact, (String)items[item]);
                    perfomCall(items[item]);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    private void perfomCall(CharSequence phone){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                MainActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) !=  PackageManager.PERMISSION_GRANTED){
            MainActivity.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_KEY);
        }else{
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse(("tel:" + phone)));
            MainActivity.this.startActivity(i);
        }
    }

    private void addContactToRecent(Contact contact, String last){
        int index = contactsRecent_list.indexOf(contact);
        contact.upCantCalls();
        contact.setLastCalled(last);
        if(index>= 0){
            contactsRecentAdapter.notifyItemChanged(index);
            contactsRecent_list.remove(index);
            contactsRecentAdapter.notifyItemRemoved(index);
            contactsRecentAdapter.notifyItemRangeChanged(index, contactsRecent_list.size());
        }

        contactsRecent_list.add(0, contact);
        contactsRecentAdapter.notifyItemInserted(0);
        contactsRecentAdapter.notifyItemRangeChanged(0, contactsRecent_list.size());
        contactsRecentAdapter.notifyDataSetChanged();
    }

    public boolean isLandscape(){
        return getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}