package com.example.pv239_android;

import android.Manifest;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.pv239_android.model.Event;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbarToady;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private Messenger mService;
    private Messenger messenger;
    private boolean bound;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started");
        toolbarToady = findViewById(R.id.toolbar_today);
        setSupportActionBar(toolbarToady);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Add fragments
        adapter.addFragment(new TabFragment(), MainActivity.this.getResources().getString(R.string.today));
        adapter.addFragment(new TabFragment(), MainActivity.this.getResources().getString(R.string.upcoming));
        //init Realm
        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        Realm mRealm = Realm.getDefaultInstance();

        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        //start tracking service
        class IncomingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AppTrackingService.MSG_LOCATION_CHANGED:
                        Log.d(TAG, "DATA HAS CHANGED");
                        adapter.notifyDataSetChanged();
                        viewPager.setAdapter(adapter);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        }
        messenger = new Messenger(new IncomingHandler());
        if(!bound) {
            Intent intent = new Intent(this, AppTrackingService.class);
            startService(intent);
            bindService(intent, trackingServiceConnection, Context.BIND_AUTO_CREATE);
        }


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

    private ServiceConnection trackingServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, AppTrackingService.MSG_REGISTER_CLIENT);
                msg.replyTo = messenger;
                mService.send(msg);
                bound = true;

            } catch (RemoteException e) {
                // Here, the service has crashed even before we were able to connect
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            bound = false;
        }

    };

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (trackingServiceConnection != null) {
            unbindService(trackingServiceConnection);
        }
    }
}
