package com.example.pv239_android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pv239_android.model.Details;
import com.example.pv239_android.model.Event;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private List<Event> eventList = new ArrayList<>();

    public EventAdapter(@NonNull Context context, List<Event> list) {
        super(context, 0 , list);
        mContext = context;
        eventList = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.event_list_view, parent,false);

        Event currentEvent= eventList.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        TextView name = (TextView) listItem.findViewById(R.id.eventName);
        name.setText(currentEvent.getmName());

        TextView time = (TextView) listItem.findViewById(R.id.eventTime);
        //TODO uncomment as soon as save plan is implemented
        time.setText(LocalDateTime.ofInstant(
                currentEvent.getmStartTime().toInstant(), ZoneId.systemDefault()).format(formatter) +
                " - " + LocalDateTime.ofInstant(
                currentEvent.getmEndTime().toInstant(), ZoneId.systemDefault()).format(formatter));
/*
        TextView mPosition = (TextView) listItem.findViewById(R.id.position);
        mPosition.setText(currentEvent.getmPosition());*/

        Switch isFinished = (Switch) listItem.findViewById(R.id.isFinished);
        isFinished.setChecked(currentEvent.ismFinished());

        ImageButton editNotes = (ImageButton) listItem.findViewById(R.id.editNotes);
        editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext , EditNotesActivity.class);
                mContext.startActivity(i);
            }
        });
        listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(mContext , Details.class);
                i.putExtra("event_id", eventList.get(position).getmId());
                mContext.startActivity(i);
                return false;
            }
        });

        return listItem;
    }

}
