package com.example.pv239_android;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pv239_android.model.Event;
import com.example.pv239_android.model.EventItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    private static final String TAG = "Tab Fragment";

    private RecyclerView recyclerView;
    private List<EventItem> eventList;
    private View v;
    private Realm mRealm;
    private String title;
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.event_recycler_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

    }

    public String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM. hh:mm");
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

    public Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public void dataChanged() {
        Log.d(TAG, "Data changed during runtime");
        loadData();
    }

    private void loadData() {
        Date today = new Date();
        mRealm = Realm.getDefaultInstance();
        RealmResults<Event> events = mRealm.where(Event.class).findAll();
        if(title.equals("Today")) {
            events = mRealm.where(Event.class)
                    .between("mEndTime", atStartOfDay(today), atEndOfDay(today)).or()
                    .between("mStartTime", atStartOfDay(today), atEndOfDay(today)).findAll();
        } else {
            events = mRealm.where(Event.class).greaterThan("mStartTime", atStartOfDay(new Date(new Date().getTime() + (24 * 60 * 60 * 1000)))).findAll();
        }
        eventList = new ArrayList<>();
        for(Event e : events) {
            eventList.add(new EventItem(e.getmId(), e.getmName(), e.getmNotes(), dateToString(e.getmStartTime()) + " - " + dateToString(e.getmEndTime()),
                    e.ismFinished(), true, e.getmLocation().getmAddress()) );
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
