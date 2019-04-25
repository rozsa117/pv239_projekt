package com.example.pv239_android.model;

import java.util.Date;

import io.realm.RealmObject;

public class Location extends RealmObject {

    private String mId;
    private String mName;
    private String mAddress;
    private double mLat;
    private double mLng;

    public Location() {
    }

    public Location(String mId, String mName, String mAddress, double mLat, double mLng) {
        this.mId = mId;
        this.mName = mName;
        this.mAddress = mAddress;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLng() {
        return mLng;
    }

    public void setmLng(double mLng) {
        this.mLng = mLng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mLat=" + mLat +
                ", mLng=" + mLng +
                '}';
    }
}
