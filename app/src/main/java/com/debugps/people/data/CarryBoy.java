package com.debugps.people.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class CarryBoy implements Parcelable {

    private ArrayList<Contact> contacts_list;
    private ArrayList<Contact> contactsFav_list;
    private ArrayList<Contact> contactsRecent_list;
    private ArrayList<Contact> contacts_list_query;
    private ArrayList<Contact> contactsFav_list_query;
    private ArrayList<Contact> contactsRecent_list_query;

    public CarryBoy() {
    }

    public CarryBoy(ArrayList<Contact> contacts_list, ArrayList<Contact> contactsFav_list, ArrayList<Contact> contactsRecent_list) {
        this.contacts_list = contacts_list;
        this.contactsFav_list = contactsFav_list;
        this.contactsRecent_list = contactsRecent_list;
    }

    protected CarryBoy(Parcel in) {
        contacts_list = in.createTypedArrayList(Contact.CREATOR);
        contactsFav_list = in.createTypedArrayList(Contact.CREATOR);
        contactsRecent_list = in.createTypedArrayList(Contact.CREATOR);
    }

    public static final Creator<CarryBoy> CREATOR = new Creator<CarryBoy>() {
        @Override
        public CarryBoy createFromParcel(Parcel in) {
            return new CarryBoy(in);
        }

        @Override
        public CarryBoy[] newArray(int size) {
            return new CarryBoy[size];
        }
    };

    public ArrayList<Contact> getContacts_list() {
        return contacts_list;
    }

    public void setContacts_list(ArrayList<Contact> contacts_list) {
        this.contacts_list = contacts_list;
    }

    public ArrayList<Contact> getContactsFav_list() {
        return contactsFav_list;
    }

    public void setContactsFav_list(ArrayList<Contact> contactsFav_list) {
        this.contactsFav_list = contactsFav_list;
    }

    public ArrayList<Contact> getContactsRecent_list() {
        return contactsRecent_list;
    }

    public void setContactsRecent_list(ArrayList<Contact> contactsRecent_list) {
        this.contactsRecent_list = contactsRecent_list;
    }

    public ArrayList<Contact> getContacts_list_query() {
        return contacts_list_query;
    }

    public void setContacts_list_query(ArrayList<Contact> contacts_list_query) {
        this.contacts_list_query = contacts_list_query;
    }

    public ArrayList<Contact> getContactsFav_list_query() {
        return contactsFav_list_query;
    }

    public void setContactsFav_list_query(ArrayList<Contact> contactsFav_list_query) {
        this.contactsFav_list_query = contactsFav_list_query;
    }

    public ArrayList<Contact> getContactsRecent_list_query() {
        return contactsRecent_list_query;
    }

    public void setContactsRecent_list_query(ArrayList<Contact> contactsRecent_list_query) {
        this.contactsRecent_list_query = contactsRecent_list_query;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(contacts_list);
        dest.writeTypedList(contactsFav_list);
        dest.writeTypedList(contactsRecent_list);
    }
}
