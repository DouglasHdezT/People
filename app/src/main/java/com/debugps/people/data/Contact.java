package com.debugps.people.data;

import android.graphics.Bitmap;

public class Contact {

    private String name;
    private String phoneNumbers;
    private String email;
    private String birthday;
    private boolean favorite;
    private Bitmap profileImage;

    public Contact() {
    }

    public Contact(String name, String phoneNumbers, String email, String birthday, boolean favorite, Bitmap profileImage) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.email = email;
        this.birthday = birthday;
        this.favorite = favorite;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
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
}
