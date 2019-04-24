package com.example.pv239_android;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.pv239_android.model.Event;
import com.google.android.gms.location.LocationListener;

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
    private Toolbar toolbarToady;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private AppTrackingService mService;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");
        toolbarToady = findViewById(R.id.toolbar_today);
        setSupportActionBar(toolbarToady);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Add fragments
        adapter.AddFragment(new TabFragment(), "Today");
        adapter.AddFragment(new TabFragment(), "Upcoming");

        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //init Realm
        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        Realm mRealm = Realm.getDefaultInstance();

        //start tracking service
        Intent intent = new Intent(this, AppTrackingService.class);
        startService(intent);

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
}
