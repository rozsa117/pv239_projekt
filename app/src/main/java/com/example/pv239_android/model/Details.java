package com.example.pv239_android.model;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pv239_android.R;

import java.time.LocalDate;

public class Details extends AppCompatActivity {

    private String name;
    private String description;
    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_details);
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
