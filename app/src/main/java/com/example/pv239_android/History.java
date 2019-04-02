package com.example.pv239_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class History extends AppCompatActivity {

    private static final String TAG = "History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Log.d(TAG, "onCreate: Started");
    }
}
