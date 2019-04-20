package com.example.pv239_android;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pv239_android.model.Event;
import com.example.pv239_android.utils.Support;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";

    Realm mRealm = Realm.getDefaultInstance();
    private EditText startTimeEditText, startDateEditText, endDateEditText, endTimeEditText;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Log.d(TAG, "onCreate: Started");

        //start time and date text view
        startTimeEditText = findViewById(R.id.new_event_edit_start_time);
        startDateEditText = findViewById(R.id.new_event_edit_start_date);

        //end time and date text view
        endDateEditText = findViewById(R.id.new_event_edit_end_date);
        endTimeEditText = findViewById(R.id.new_event_edit_end_time);

        //setting listeners to each button and setting current date and time on a new event
        handleDateAndTimeSelection();

        final Intent incomingIntent = getIntent();
        //passed by the CalendarActivity, format year-month-day
        int[] date = incomingIntent.getIntArrayExtra("date");

        final Calendar c = Calendar.getInstance();
        if (date != null) {
            //for start date and time
            saveAndPrintDate(date[0], date[1], date[2], true);
            saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

            //for start date and time
            saveAndPrintDate(date[0], date[1], date[2], false);
            saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY ) + 1, c.get(Calendar.MINUTE), false);
        } else {
            //for start date and time
            saveAndPrintDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), true);
            saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

            //for start date and time
            saveAndPrintDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), false);
            saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY) + 1, c.get(Calendar.MINUTE), false);
        }


        // Save button
        Button saveButton = (Button) findViewById(R.id.btnSavePlan);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: Save plan");
                final Date startDate = Support.getDate(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute);
                final Date endDate = Support.getDate(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute);
                if (startDate.before(endDate)) {
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Event mEvent = new Event();
                            mEvent.setmId(getNextKey());
                            mEvent.setmName(((EditText) findViewById(R.id.newEventNameEdit)).getText().toString());
                            mEvent.setmDescription(((EditText) findViewById(R.id.newEventDescriptionEdit)).getText().toString());
                            mEvent.setmStartTime(startDate);
                            mEvent.setmEndTime(endDate);
                            mEvent.setmNotes(((EditText) findViewById(R.id.newEventNotes)).getText().toString());
                            //TODO finish implementation
                            //mEvent.setmPosition();

                            realm.insertOrUpdate(mEvent);
                        }
                    });
                    // go back to main page
                    startActivity(new Intent(NewEventActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(NewEventActivity.this,
                            NewEventActivity.this.getResources().getString(R.string.toast_start_before_end),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Needed for creation of a new even. Gets the next eid for the event.
     * @return new id for a new event
     */
    private int getNextKey() {
        try {
            Number number = mRealm.where(Event.class).max("mId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    private void handleDateAndTimeSelection() {

        //start date
        startDateEditText.setOnClickListener(makeListenerForDate(true));

        //start time
        startTimeEditText.setOnClickListener(makeListenerForTime(true));

        // end date
        endDateEditText.setOnClickListener(makeListenerForDate(false));

        // end time
        endTimeEditText.setOnClickListener(makeListenerForTime(false));
    }

    private  View.OnClickListener makeListenerForTime(final boolean isStart) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEventActivity.this, R.style.CustomDialogTheme,
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


                final DatePickerDialog datePickerDialog = new DatePickerDialog(NewEventActivity.this, R.style.CustomDialogTheme, null, mStartYear, mStartMonth, mStartDay);
//                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                    }
//                });
                //date picker is closed when date is selected
                datePickerDialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        saveAndPrintDate(year, monthOfYear, dayOfMonth, isStart);
                        datePickerDialog.dismiss();
                    }
                });
                datePickerDialog.show();

            }
        };
    }

    private void setTimeAndDateIfNeeded(boolean isStart) {
        final Calendar c = Calendar.getInstance();
        saveAndPrintDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), isStart);
        saveAndPrintTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), isStart);
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
