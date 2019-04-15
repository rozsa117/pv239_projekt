package com.example.pv239_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.pv239_android.model.Event;
import com.example.pv239_android.model.EventItem;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    private RecyclerView recyclerView;
    private List<EventItem> historyEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Event> events = mRealm.where(Event.class).lessThan("mEndTime", atEndOfDay(new Date(new Date().getTime() - (24 * 60 * 60 * 1000)))).findAll();
        historyEventList = new ArrayList<>();
        for(Event e : events) {
            historyEventList.add(new EventItem(e.getmName(), e.getmPosition(), e.getmNotes(), dateToString(e.getmStartTime()) + " - " + dateToString(e.getmEndTime()), e.ismFinished(), false));
        }

        //v = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = (RecyclerView) findViewById(R.id.history_event_recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this.getBaseContext(), historyEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getBaseContext(),
                DividerItemDecoration.VERTICAL));
        Log.d(TAG, "onCreate: Started");
    }

    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd hh:mm");
        return dateFormat.format(date);

    }

    public Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


}
