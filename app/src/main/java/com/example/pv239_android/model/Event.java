package com.example.pv239_android.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {

    @PrimaryKey
    private int mId;
    private String mName;
    private String mDescription;
    private String mNotes;
    private Date mStartTime;
    private Date mEndTime;
    private Location mLocation;
    private boolean mFinished = false;

    public Event(String mName, String mDescription, String mNotes, Date mStartTime, Date mEndTime, Location mLocation, boolean mFinished) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mNotes = mNotes;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mLocation = mLocation;
        this.mFinished = mFinished;
    }

    public Event() {

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public Date getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Date getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(Date mEndTime) {
        this.mEndTime = mEndTime;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public boolean ismFinished() {
        return mFinished;
    }

    public void setmFinished(boolean mFinished) {
        this.mFinished = mFinished;
    }


}
