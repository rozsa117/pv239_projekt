package com.example.pv239_android;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.pv239_android.model.Event;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");

        //init Realm
        Realm.init(getApplicationContext());
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        Realm mRealm = Realm.getDefaultInstance();

        //***********************************************************//
        //TODO DELETE THIS PART AS SOON AS YOU WANT DATA IN APLICATION
        /*mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Event.class);
            }
        });*/
        //***********************************************************//
        //get all Events
        RealmResults<Event> events = mRealm.where(Event.class).findAll();
        EventAdapter adapter = new EventAdapter(this,
                events);

        ListView listView = (ListView) findViewById(R.id.event_list);
        listView.setAdapter(adapter);

        // new Event button
        Button createNew = (Button) findViewById(R.id.newEvent);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Crate new plan");
                Intent i = new Intent(MainActivity.this, NewEventActivity.class);
                startActivity(i);
            }
        });

        // CalendarActivity button
        Button calendarBtn = (Button) findViewById(R.id.btnCalendar);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: CalendarActivity");
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });

        // HistoryActivity button
        Button historyBtn = (Button) findViewById(R.id.btnHistory);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: HistoryActivity");
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

    }

    // TODO converts string to date, maybe should be placed somewhere else
    // possible palce is a static class in order to access form anywhere
    public Date toDate(String dateString) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            date = formatter.parse(dateString);

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;
    }
}
