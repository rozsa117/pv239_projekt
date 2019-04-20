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
import android.widget.Toast;

import com.example.pv239_android.MainActivity;
import com.example.pv239_android.NewEventActivity;
import com.example.pv239_android.R;
import com.example.pv239_android.utils.Support;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class Details extends AppCompatActivity {

//    Realm mRealm = Realm.getDefaultInstance();

    private static final String TAG = "Details";
    Realm mRealm = Realm.getDefaultInstance();
    private EditText startDateEditText, startTimeEditText, endDateEditText, endTimeEditText;
    private EditText editName, editDescription, editLocation, editNotes;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    private Event thisEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_details);

        editName = (EditText) findViewById(R.id.details_edit_name);
        editDescription = (EditText) findViewById(R.id.details_edit_description);
        editNotes = (EditText) findViewById(R.id.details_notes);
        //start time editText
        startDateEditText = (EditText) findViewById(R.id.details_edit_start_date);
        //start time editText
        startTimeEditText = (EditText) findViewById(R.id.details_edit_start_time);
        // end date editText
        endDateEditText = (EditText) findViewById(R.id.details_edit_end_date);
        // end time editText
        endTimeEditText = (EditText) findViewById(R.id.details_edit_end_time);

        final Intent incomingIntent = getIntent();
        final int incoming_id = incomingIntent.getIntExtra("event_id", -1);

        Event event = mRealm.where(Event.class).equalTo("mId", incoming_id).findFirst();
        editName.setText(event.getmName());
        editDescription.setText(event.getmDescription());
        editNotes.setText(event.getmNotes());
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(event.getmStartTime());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(event.getmEndTime());
        saveAndPrintDate(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), true);
        saveAndPrintDate(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH), false);
        saveAndPrintTime(startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE), true);
        saveAndPrintTime(endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE), false);
        handleDateAndTimeSelection();
        // Save button
        Button updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Date startDate = Support.getDate(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute);
                Date endDate = Support.getDate(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute);
                if (startDate.before(endDate)) {
                    mRealm.beginTransaction();
                    Event event = mRealm.where(Event.class).equalTo("mId", incoming_id).findFirst();
                    event.setmName(editName.getText().toString());
                    event.setmDescription(editDescription.getText().toString());
                    event.setmStartTime(startDate);
                    event.setmEndTime(endDate);
                    event.setmNotes(editNotes.getText().toString());
                    mRealm.insertOrUpdate(event);
                    mRealm.commitTransaction();
                    startActivity(new Intent(Details.this, MainActivity.class));
                }
                else{
                    Toast.makeText(Details.this,
                            Details.this.getResources().getString(R.string.toast_start_before_end),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Save button
        Button deleteButton = (Button) findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.beginTransaction();
                Event event = mRealm.where(Event.class)
                        .equalTo("mId", incoming_id).findFirst();
                event.deleteFromRealm();
                mRealm.commitTransaction();
                startActivity(new Intent(Details.this, MainActivity.class));
            }
        });
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
//        startDateEditText.setText(thisEvent.getmStartTime().dateToString());
//        endDateEditText.setText(thisEvent.getmEndTime().dateToString());



    }

    private void handleDateAndTimeSelection() {
        startDateEditText.setOnClickListener(makeListenerForDate(true));

        startTimeEditText.setOnClickListener(makeListenerForTime(true));

        endDateEditText.setOnClickListener(makeListenerForDate(false));

        endTimeEditText.setOnClickListener(makeListenerForTime(false));
    }

    private  View.OnClickListener makeListenerForTime(final boolean isStart) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Details.this, R.style.CustomDialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                saveAndPrintTime(hourOfDay, minute, isStart);
                            }
                        }, mStartHour, mStartMinute, true);
                timePickerDialog.show();
            }
        };
    }

    private View.OnClickListener makeListenerForDate(final boolean isStart) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(Details.this, R.style.CustomDialogTheme, null, mStartYear, mStartMonth, mStartDay);

                datePickerDialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        saveAndPrintDate(year, monthOfYear, dayOfMonth, isStart);
                        datePickerDialog.dismiss();
                    }
                });
//                DatePickerDialog datePickerDialog = new DatePickerDialog(Details.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                saveAndPrintDate(year, monthOfYear, dayOfMonth, isStart);
//                            }
//                        }, mStartYear, mStartMonth, mStartDay);
                datePickerDialog.show();
            }
        };
    }

    private void saveAndPrintTime(int hour, int minute, boolean isStart) {
        String formatted = String.format( "%02d", hour)+ ":" + String.format( "%02d", minute);
        if(isStart) {
            startTimeEditText.setText(formatted);
            mStartHour = hour;
            mStartMinute = minute;
        }
        else {
            endTimeEditText.setText(formatted);
            mEndHour = hour;
            mEndMinute = minute;

        }
    }
    private void saveAndPrintDate(int year, int month, int day, boolean isStart) {
        String formatted = String.format("%02d", day) + "-" + String.format("%02d", (month + 1)) + "-" + year;
        if (isStart) {
            startDateEditText.setText(formatted);
            mStartYear = year;
            mStartMonth = month;
            mStartDay = day;
        }
        else {
            endDateEditText.setText(formatted);
            mEndYear = year;
            mEndMonth = month;
            mEndDay = day;
        }
    }
}
