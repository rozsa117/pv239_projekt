package com.example.pv239_android.model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.pv239_android.NewEventActivity;
import com.example.pv239_android.R;

import java.util.Calendar;

import io.realm.Realm;

public class Details extends AppCompatActivity {

//    Realm mRealm = Realm.getDefaultInstance();

    private static final String TAG = "Details";
//    private EditText startDateEditText, startTimeEditText, endDateEditText, endTimeEditText;
//    private EditText editName, editDescription, editLocation, editNotes;
//    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
//    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
//    private Event thisEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_details);
//        handleDateAndTimeSelection();
//        final Intent incomingIntent = getIntent();
//        final int eventId = incomingIntent.getIntExtra("event_id", 0);
//        Log.d(TAG, "Domino " + eventId);
//
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                thisEvent = realm.where(Event.class).equalTo("mId", eventId).findFirst();
//            }
//        });
//
//        startDateEditText.setText(thisEvent.getmStartTime().toString());
//        endDateEditText.setText(thisEvent.getmEndTime().toString());



    }

//    private void handleDateAndTimeSelection() {
//
//        //start time editText
//        startDateEditText = (EditText) findViewById(R.id.details_edit_start_date);
//        startDateEditText.setOnClickListener(makeListenerForDate(true));
//
//        //start time editText
//        startTimeEditText = (EditText) findViewById(R.id.details_edit_start_time);
//        startTimeEditText.setOnClickListener(makeListenerForTime(true));
//
//        // end date editText
//        endDateEditText = (EditText) findViewById(R.id.details_edit_end_date);
//        endDateEditText.setOnClickListener(makeListenerForDate(false));
//
//        // end time editText
//        endTimeEditText = (EditText) findViewById(R.id.details_edit_end_time);
//        endTimeEditText.setOnClickListener(makeListenerForTime(false));
//
//        //name editText
//        editName = (EditText) findViewById(R.id.details_edit_name);
//        editName.setOnClickListener(makeListenerForDate(true));
//
//        //description editText
//        editDescription= (EditText) findViewById(R.id.details_edit_description);
//        editDescription.setOnClickListener(makeListenerForTime(true));
//
//        //location editText
//        editLocation = (EditText) findViewById(R.id.details_edit_location);
//        editLocation.setOnClickListener(makeListenerForDate(false));
//
//        //notes editText
//        editNotes = (EditText) findViewById(R.id.details_notes);
//        editNotes.setOnClickListener(makeListenerForTime(false));
//    }
//
//    private  View.OnClickListener makeListenerForTime(final boolean isStart) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Launch Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(Details.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//                                saveAndPrintTime(hourOfDay, minute, isStart);
//                            }
//                        }, mStartHour, mStartMinute, true);
//                timePickerDialog.show();
//            }
//        };
//    }
//
//    private View.OnClickListener makeListenerForDate(final boolean isStart) {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(Details.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                saveAndPrintDate(year, monthOfYear, dayOfMonth, isStart);
//                            }
//                        }, mStartYear, mStartMonth, mStartDay);
//                datePickerDialog.show();
//            }
//        };
//    }
//
//    private void setTimeAndDateIfNeeded(boolean isStart) {
//        final Calendar c = Calendar.getInstance();
//        saveAndPrintDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), isStart);
//        saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), isStart);
//    }
//
//    private void saveAndPrintTime(int hour, int minute, boolean isStart) {
//        if(isStart) {
//            startTimeEditText.setText(hour + ":" + minute);
//            mStartHour = hour;
//            mStartMinute = minute;
//        }
//        else {
//            endTimeEditText.setText(hour + ":" + minute);
//            mEndHour = hour;
//            mEndMinute = minute;
//
//        }
//    }
//
//    private void saveAndPrintDate(int year, int month, int day, boolean isStart) {
//        if (isStart) {
//            startDateEditText.setText(day + "-" + (month + 1) + "-" + year);
//            mStartYear = year;
//            mStartMonth = month;
//            mStartDay = day;
//        }
//        else {
//            endDateEditText.setText(day + "-" + (month + 1) + "-" + year);
//            mEndYear = year;
//            mEndMonth = month;
//            mEndDay = day;
//        }
//    }
}
