package com.example.pv239_android;

import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        Log.d(TAG, "onCreate: Started");

        CalendarView mCalendarView = (CalendarView) findViewById(R.id.calendar);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(CalendarActivity.this, NewEventActivity.class);
                intent.putExtra("date", new int[]{year, month, dayOfMonth});
                startActivity(intent);
            }
        });
    }
}
