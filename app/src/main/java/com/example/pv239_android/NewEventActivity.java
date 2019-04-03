package com.example.pv239_android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pv239_android.model.Event;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.realm.Realm;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";

    Realm mRealm = Realm.getDefaultInstance();
    Date incomingDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Log.d(TAG, "onCreate: Started");

        LocalDateTime now =  LocalDateTime.now();
        final Intent incomingIntent = getIntent();
        //passed by the CalendarActivity, format year-month-day
        int[] date = incomingIntent.getIntArrayExtra("date");
        if (date != null){
            incomingDate = new Date (date[0], date[1], date[2], now.getHour(), now.getMinute());
        } else {
            incomingDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        }

        //TODO set the start and end time of an event to user in order to allow him to edit and see the value

        // Save button
        Button saveButton = (Button) findViewById(R.id.btnSavePlan);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: Save plan");

                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Event mEvent = new Event();
                        mEvent.setmId(getNextKey());
                        mEvent.setmName(((EditText) findViewById(R.id.newEventNameEdit)).getText().toString());
                        mEvent.setmDescription(((EditText) findViewById(R.id.newEventDescriptionEdit)).getText().toString());
                        mEvent.setmStartTime(incomingDate);
                        //set time an hour later
                        incomingDate.setTime(incomingDate.getTime() + 3600);
                        mEvent.setmEndTime(incomingDate);
                        //TODO finish implementation
                        //mEvent.setmPosition();

                        realm.insert(mEvent);
                    }
                });

                // go back to main page
                startActivity(new Intent(NewEventActivity.this, MainActivity.class));
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

}
