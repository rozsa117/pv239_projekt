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
import com.example.pv239_android.model.Location;
import com.example.pv239_android.utils.Support;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.libraries.places.compat.ui.PlacePicker;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";

    Realm mRealm = Realm.getDefaultInstance();
    private TextView startTimeTextView, startDateTextView, endDateTextView, endTimeTextView, locationView;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    private Location actualLocation = null;

    private final static int PLACE_PICKER_REQUEST = 111;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Log.d(TAG, "onCreate: Started");

        //start time and date text view
        startTimeTextView = findViewById(R.id.newEventStartTimeTextView);
        startDateTextView = findViewById(R.id.newEventStartDateTextView);

        //end time and date text view
        endDateTextView = findViewById(R.id.newEventEndDateTextView);
        endTimeTextView = findViewById(R.id.newEventEndTimeTextView);

        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacePicker();
            }
        });

        // Initialize Places with new version of dependency
//        Places.initialize(getApplicationContext(), "AIzaSyAkCaEXBLSiELz6uu2JFvviwVME1dSU4lk");
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), "AIzaSyAkCaEXBLSiELz6uu2JFvviwVME1dSU4lk");
//        }
//
//        // Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(this);

        locationView = findViewById(R.id.locationAutocomplete);
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAutocomplete();
            }
        });

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
                if (startDate.before(endDate) && actualLocation != null) {
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Event mEvent = new Event();
                            mEvent.setmId(getNextKey());
                            mEvent.setmName(((EditText) findViewById(R.id.newEventNameEdit)).getText().toString());
                            mEvent.setmDescription(((EditText) findViewById(R.id.newEventDescriptionEdit)).getText().toString());
                            mEvent.setmStartTime(startDate);
                            mEvent.setmEndTime(endDate);
                            mEvent.setmLocation(actualLocation);
                            //TODO finish implementation

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

        //start time button
        Button startDateButton = (Button) findViewById(R.id.newEventSelectStartDateButton);
        startDateButton.setOnClickListener(makeListenerForDate(true));

        //start time button
        Button startTimeButton = (Button) findViewById(R.id.newEventSelectStartTimeButton);
        startTimeButton.setOnClickListener(makeListenerForTime(true));

        // end date button
        Button endDateButton = (Button) findViewById(R.id.newEventSelectEndDateButton);
        endDateButton.setOnClickListener(makeListenerForDate(false));

        // end time button
        Button endTimeButton = (Button) findViewById(R.id.newEventSelectEndTimeButton);
        endTimeButton.setOnClickListener(makeListenerForTime(false));
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
            startTimeTextView.setText(formatted);
            mStartHour = hour;
            mStartMinute = minute;
        }
        else {
            endTimeTextView.setText(formatted);
            mEndHour = hour;
            mEndMinute = minute;
        }
    }

    private void saveAndPrintDate(int year, int month, int day, boolean isStart) {
        String formatted = String.format("%02d", day) + "-" + String.format("%02d", (month + 1)) + "-" + year;
        if (isStart) {
            startDateTextView.setText(formatted);
            mStartYear = year;
            mStartMonth = month;
            mStartDay = day;
        }
        else {
            endDateTextView.setText(formatted);
            mEndYear = year;
            mEndMonth = month;
            mEndDay = day;
        }
    }

    private void initAutocomplete() {
        EditText btnAutocomplete = (EditText) findViewById(R.id.locationAutocomplete);
        // Set the fields to specify which types of place data to return.
        //List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

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
                locationView.setText(place.getName() +  " , " + place.getAddress());
                actualLocation = new Location(place.getId(), place.getName().toString(), place.getAddress().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
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

    private void openPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}
