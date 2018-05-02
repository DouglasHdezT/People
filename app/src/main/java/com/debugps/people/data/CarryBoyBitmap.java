package com.debugps.people.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class CarryBoyBitmap implements Parcelable {
    private byte[] byteArray;
    private int height;
    private int width;
    private Bitmap.Config bitmapConfig;

    public CarryBoyBitmap(byte[] byteArray, int height, int width, Bitmap.Config bitmapConfig) {
        this.byteArray = byteArray;
        this.height = height;
        this.width = width;
        this.bitmapConfig = bitmapConfig;
    }

    protected CarryBoyBitmap(Parcel in) {
        byteArray = in.createByteArray();
        height = in.readInt();
        width = in.readInt();
    }

    public static final Creator<CarryBoyBitmap> CREATOR = new Creator<CarryBoyBitmap>() {
        @Override
        public CarryBoyBitmap createFromParcel(Parcel in) {
            return new CarryBoyBitmap(in);
        }

        @Override
        public CarryBoyBitmap[] newArray(int size) {
            return new CarryBoyBitmap[size];
        }
    };

    public byte[] getByteArray() {
        return byteArray;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(byteArray);
        dest.writeInt(height);
        dest.writeInt(width);
    }
}
