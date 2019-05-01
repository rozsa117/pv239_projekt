package com.example.pv239_android;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pv239_android.model.Event;
import com.example.pv239_android.model.Location;
import com.example.pv239_android.utils.Support;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class DetailsActivity extends AppCompatActivity {

//    Realm mRealm = Realm.getDefaultInstance();

    private static final String TAG = "DetailsActivity";
    private Realm mRealm;
    private EditText startDateEditText, startTimeEditText, endDateEditText, endTimeEditText;
    private EditText editName, editDescription, editLocation, editNotes;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    private Event thisEvent;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Location actualLocation = null;
    private Location newLocation = null;

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
        // edit location
        editLocation = (EditText) findViewById(R.id.details_edit_location);
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAutocomplete();
            }
        });

        final Intent incomingIntent = getIntent();
        final int incoming_id = incomingIntent.getIntExtra("event_id", -1);

        Realm.init(this);
        mRealm = Realm.getDefaultInstance();


        thisEvent = mRealm.where(Event.class).equalTo("mId", incoming_id).findFirst();
        editName.setText(thisEvent.getmName());
        editDescription.setText(thisEvent.getmDescription());
        actualLocation = thisEvent.getmLocation();
        editLocation.setText(thisEvent.getmLocation().getmAddress());
        editNotes.setText(thisEvent.getmNotes());
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(thisEvent.getmStartTime());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(thisEvent.getmEndTime());
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
                if (startDate.before(endDate) && actualLocation != null) {
                    mRealm.beginTransaction();
                    thisEvent.setmName(editName.getText().toString());
                    thisEvent.setmDescription(editDescription.getText().toString());
                    thisEvent.setmStartTime(startDate);
                    thisEvent.setmEndTime(endDate);
                    if(newLocation != null) {
                        actualLocation.setmAddress(newLocation.getmAddress());
                        actualLocation.setmLat(newLocation.getmLat());
                        actualLocation.setmLng(newLocation.getmLng());
                        actualLocation.setmName(newLocation.getmName());
                    }

                    thisEvent.setmLocation(actualLocation);
                    thisEvent.setmNotes(editNotes.getText().toString());
                    mRealm.insertOrUpdate(thisEvent);
                    mRealm.commitTransaction();
                    startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                }
                else{
                    if (actualLocation != null) {
                        Toast.makeText(DetailsActivity.this,
                                DetailsActivity.this.getResources().getString(R.string.toast_empty_location),
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(DetailsActivity.this,
                                DetailsActivity.this.getResources().getString(R.string.toast_start_before_end),
                                Toast.LENGTH_LONG).show();
                    }
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
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            }
        });



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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this, R.style.CustomDialogTheme,
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, R.style.CustomDialogTheme, null, mStartYear, mStartMonth, mStartDay);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        saveAndPrintDate(year, month, dayOfMonth, isStart);

                    }
                });
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

    private void initAutocomplete() {
        // Start the autocomplete intent.
        Intent intent = null;
        try {
            intent = new PlaceAutocomplete.IntentBuilder(
                    AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                editLocation.setText(place.getAddress());
                newLocation = new Location(place.getId(), place.getName().toString(), (String) place.getAddress(), place.getLatLng().latitude, place.getLatLng().longitude);
                Log.i(TAG, "Place I found: " + place.getName() + ", " + place.getId() + ", " + place.getAddress() + ", " + place.getLatLng());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = PlaceAutocomplete.getStatus(this,data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
