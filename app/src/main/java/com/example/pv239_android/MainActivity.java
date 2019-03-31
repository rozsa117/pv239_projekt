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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("test.db")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Log.d(TAG, "onCreate: Started");
        Realm mRealm = Realm.getInstance(configuration);

        ArrayList<Event> eventsList = new ArrayList<>();
        eventsList.add(new Event("Gym", "go to gym", "12Ssa", "take bottle of water", toDate("2018-09-24 09:30:00")
        ,toDate("2018-09-24 10:30:00"), false));
        eventsList.add(new Event("School", "go to gym", "12Ssa", "take bottle of water", toDate("2018-09-26 16:00:00")
                , toDate("2018-09-29 17:00:00"), false));

        EventAdapter adapter = new EventAdapter(this,
                eventsList);

        ListView listView = (ListView) findViewById(R.id.event_list);
        listView.setAdapter(adapter);


        Button createNew = (Button) findViewById(R.id.newTask);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Crate new plan");
                Intent i = new Intent(MainActivity.this, NewEventActivity.class);
                startActivity(i);
            }
        });

        Button calendarBtn = (Button) findViewById(R.id.btnCalendar);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: Calendar");
                startActivity(new Intent(MainActivity.this, Calendar.class));
            }
        });

        Button historyBtn = (Button) findViewById(R.id.btnHistory);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: History");
                startActivity(new Intent(MainActivity.this, Calendar.class));
            }
        });

    }

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
