package com.example.pv239_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.pv239_android.model.Event;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EditNotesActivity extends AppCompatActivity {

    private static final String TAG = "EditNotesActivity";
    private EditText editText;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealm();
        setContentView(R.layout.activity_edit_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.editNotes);
        editText.setText(getIntent().getStringExtra("notes"));
    }






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back pressed");
        Event event = mRealm.where(Event.class).equalTo("mId", getIntent().getIntExtra("eventId",0)).findFirst();
        mRealm.beginTransaction();
       if(event != null) {
           event.setmNotes(editText.getText().toString());
           mRealm.insertOrUpdate(event);
       }
       mRealm.commitTransaction();
       Log.d(TAG, "EVENT FOUND " + event.toString());

       Intent intent = new Intent(getApplicationContext(),MainActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       getApplicationContext().startActivity(intent);
    }



    // create an action bar button

    // handle button activities

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "Clicked on options menu id : " + id );
        Log.d(TAG, "id of home: " + R.id.homeAsUp);
        if (id == android.R.id.home) {
            Log.d(TAG, "it is it");
            this.onBackPressed();
        }
        return true;
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getDefaultInstance();
    }
}
