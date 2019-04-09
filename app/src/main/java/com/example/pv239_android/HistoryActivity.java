package com.example.pv239_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.pv239_android.model.EventItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    private RecyclerView recyclerView;
    private List<EventItem> historyEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        historyEventList = new ArrayList<>();
        historyEventList.add(new EventItem("Gym", "Bratislava", "take watter", "10:00-12:00"));
        historyEventList.add(new EventItem("School", "Zilina", "take pen", "15:00-17:00"));


        //v = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = (RecyclerView) findViewById(R.id.history_event_recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this.getBaseContext(), historyEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getBaseContext(),
                DividerItemDecoration.VERTICAL));
        Log.d(TAG, "onCreate: Started");
    }
}
