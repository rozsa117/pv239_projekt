package com.example.pv239_android.model;

import java.util.Date;

public class EventItem {

    private int mEventId;
    private String mName;
    private String mNotes;
    private String mTime;
    private Boolean isFinished;
    private Boolean canBeEdited;
    private String mLocation;

    public EventItem(int mEventId, String mName, String mNotes, String mTime, Boolean isFinished, Boolean canBeEdited, String mLocation) {
        this.mName = mName;
        this.mNotes = mNotes;
        this.mTime = mTime;
        this.isFinished = isFinished;
        this.canBeEdited = canBeEdited;
        this.mLocation = mLocation;
        this.mEventId = mEventId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }


    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }


    public Boolean getCanBeEdited() {
        return canBeEdited;
    }

    public void setCanBeEdited(Boolean canBeEdited) {
        this.canBeEdited = canBeEdited;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public int getmEventId() {
        return mEventId;
    }

    public void setmEventId(int mEventId) {
        this.mEventId = mEventId;
    }
}
