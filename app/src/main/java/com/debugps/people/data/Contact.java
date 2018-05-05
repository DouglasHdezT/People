package com.debugps.people.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Contact implements Parcelable {

    private String name;
    private ArrayList<String> phoneNumbers;
    private String email;
    private String birthday;
    private boolean favorite = false;
    private Bitmap profileImage;
    private int cantCalls = 0 ;
    private int colorId;
    private String lastCalled;

    CharSequence[] strings;

    public Contact() {
    }


    protected Contact(Parcel in) {
        name = in.readString();
        phoneNumbers = in.createStringArrayList();
        email = in.readString();
        birthday = in.readString();
        favorite = in.readByte() != 0;
        profileImage = in.readParcelable(Bitmap.class.getClassLoader());
        cantCalls = in.readInt();
        colorId = in.readInt();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber(int index) {
        return phoneNumbers.get(index);
    }

    public String getAllNumbers(){
        String result = "";
        for(String phone: phoneNumbers){
            result = phone + ",";
        }
        return result;
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
        strings = new CharSequence[phoneNumbers.size()];

        for(int i = 0; i< phoneNumbers.size();i++){
            strings[i] = phoneNumbers.get(i);
        }

        return strings;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
