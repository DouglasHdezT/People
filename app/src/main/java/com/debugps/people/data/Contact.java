package com.debugps.people.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    private String name;
    private String phoneNumbers;
    private String email;
    private String birthday;
    private boolean favorite;
    private Bitmap profileImage;
    private int colorId;

    public Contact() {
    }

    public Contact(String name, String phoneNumbers, String email, String birthday, boolean favorite, Bitmap profileImage, int colorId) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.email = email;
        this.birthday = birthday;
        this.favorite = favorite;
        this.profileImage = profileImage;
        this.colorId = colorId;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        phoneNumbers = in.readString();
        email = in.readString();
        birthday = in.readString();
        favorite = in.readByte() != 0;
        profileImage = in.readParcelable(Bitmap.class.getClassLoader());
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

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumbers);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeParcelable(profileImage, flags);
        dest.writeInt(colorId);
    }
}
