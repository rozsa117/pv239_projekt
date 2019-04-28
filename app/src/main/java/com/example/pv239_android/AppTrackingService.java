package com.example.pv239_android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.example.pv239_android.model.Event;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class AppTrackingService extends Service {

    private static final String TAG = "AppTrackingService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0.5f;
    private RealmResults<Event> events;
    private Realm mRealm;
    private Messenger mainActivity;
    private Messenger messenger;

    /**
     * Command to the service to display a message
     */
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_LOCATION_CHANGED = 2;


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }


        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            checkLocationOfEvents(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        messenger = new Messenger(new IncomingHandler(this));


        Criteria criteria = new Criteria();
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getDefaultInstance();
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
            mLocationManager.getBestProvider(criteria, true);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    private void checkLocationOfEvents(Location location) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 2);
        events = mRealm.where(Event.class)
                .greaterThan("mEndTime", c.getTime()).and()
                .lessThan("mStartTime", c.getTime()).findAll();
        mRealm.beginTransaction();
        for (Event e : events) {
            Log.d(TAG, e.getmLocation().toString());
            Location eventLocation = new Location("provider");
            eventLocation.setLatitude(e.getmLocation().getmLat());
            eventLocation.setLongitude(e.getmLocation().getmLng());
            if(location.distanceTo(eventLocation) < 10) {
                e.setmFinished(true);
                try {
                    if(mainActivity != null) {
                        mainActivity.send(Message.obtain(null, MSG_LOCATION_CHANGED));
                    }
                } catch (RemoteException ex) {
                    // If we get here, the client is dead, and we should remove it from the list
                    Log.d(TAG, "Removing client main activity");
                    mainActivity = null;
                }
            }
        }
        mRealm.commitTransaction();
    }

    class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.d(TAG, "Adding client: " + msg.replyTo);
                    mainActivity = msg.replyTo;
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new IncomingHandler(this));
        return messenger.getBinder();
    }
}
