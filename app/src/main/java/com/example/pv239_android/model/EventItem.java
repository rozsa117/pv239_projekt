package com.example.pv239_android.model;

import java.util.Date;

public class EventItem {

    private String mName;
    private String mPosition;
    private String mNotes;
    private String mTime;
    private Boolean isFinished;
    private Boolean canBeEdited;

    public EventItem(String mName, String mPosition, String mNotes, String mTime, Boolean isFinished, Boolean canBeEdited) {
        this.mName = mName;
        this.mPosition = mPosition;
        this.mNotes = mNotes;
        this.mTime = mTime;
        this.isFinished = isFinished;
        this.canBeEdited = canBeEdited;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
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
}
