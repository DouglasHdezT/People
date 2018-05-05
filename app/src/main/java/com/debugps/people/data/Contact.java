package com.debugps.people.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Contact implements Parcelable, Comparable<Contact> {

    //AREA DE VARIABLES.
    private boolean favorite = false;

    private String name;
    private String email;
    private String birthday;
    private String lastCalled;

    private int cantCalls = 0 ;
    private int colorId;

    private Uri profileImage;

    private ArrayList<String> phoneNumbers;

    //AREA DE METODOS
    public Contact() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getPhoneNumber(int index) {
        return phoneNumbers.get(index);
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumbers == null){
            phoneNumbers = new ArrayList<>();
        }

        phoneNumber = phoneNumber.replaceAll("\\s", "");

        if(!phoneNumbers.contains(phoneNumber) && !phoneNumber.equals("")){
            phoneNumbers.add(phoneNumber);
        }
    }

    public CharSequence[] getArrayOfPhones(){
        CharSequence[] strings = new CharSequence[phoneNumbers.size()];

        for(int i = 0; i< phoneNumbers.size();i++){
            strings[i] = phoneNumbers.get(i);
        }

        return strings;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Uri getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Uri profileImage) {
        this.profileImage = profileImage;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getCantCalls() {
        return cantCalls;
    }

    public void upCantCalls() {
        this.cantCalls = this.cantCalls+1;
    }

    public String getLastCalled() {
        return lastCalled;
    }

    public void setLastCalled(String lastCalled) {
        this.lastCalled = lastCalled;
    }

    /*
    Implementacionde metodos de Parceable
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(lastCalled);
        dest.writeInt(cantCalls);
        dest.writeInt(colorId);
        dest.writeParcelable(profileImage, flags);
        dest.writeStringList(phoneNumbers);
    }

    protected Contact(Parcel in) {
        favorite = in.readByte() != 0;
        name = in.readString();
        email = in.readString();
        birthday = in.readString();
        lastCalled = in.readString();
        cantCalls = in.readInt();
        colorId = in.readInt();
        profileImage = in.readParcelable(Uri.class.getClassLoader());
        phoneNumbers = in.createStringArrayList();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int compareTo(@NonNull Contact o) {
        return this.getName().compareTo(o.getName());
    }
}
