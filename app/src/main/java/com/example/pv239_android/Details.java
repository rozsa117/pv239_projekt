package com.example.pv239_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.time.LocalDate;

public class Details extends AppCompatActivity {

    private static final String TAG = "Details";
    private String name;
    private String description;
    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_details);

        Log.d(TAG, "onCreate: Started");

        Button saveButton = (Button) findViewById(R.id.btnSavePlan);
        saveButton.setOnClickListener((v) -> {
            Log.d(TAG, "onCreate: Save plan");

            //TODO add saving the plan

            startActivity(new Intent(Details.this, MainActivity.class));
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
