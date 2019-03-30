package com.example.pv239_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started");
        
        Button createNewBtn = (Button) findViewById(R.id.btnCreateNewPlan);
        createNewBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Crate new plan");
            startActivity(new Intent(MainActivity.this, Details.class));
        });

        Button calendarBtn = (Button) findViewById(R.id.btnCalendar);
        calendarBtn.setOnClickListener((v) -> {
            Log.d(TAG, "onCreate: Calendar");
            startActivity(new Intent(MainActivity.this, Calendar.class));
        });

        Button historyBtn = (Button) findViewById(R.id.btnHistory);
        historyBtn.setOnClickListener((v) -> {
            Log.d(TAG, "onCreate: History");
            startActivity(new Intent(MainActivity.this, Calendar.class));
        });
    }
}
