package com.example.pv239_android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pv239_android.model.EventItem;
import java.util.List;

import io.realm.Realm;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<EventItem> mData;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(Context mContext, List<EventItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_event, viewGroup, false);
        return new MyViewHolder(v, mData.get(i).getCanBeEdited());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_Name.setText(mData.get(i).getmName());
        myViewHolder.tv_Time.setText(mData.get(i).getmTime() );
        myViewHolder.tv_Location.setText(mData.get(i).getmLocation());
        myViewHolder.notes = mData.get(i).getmNotes();
        myViewHolder.eventId = mData.get(i).getmEventId();
        if(mData.get(i).getFinished()) {
            myViewHolder.isFinished.setImageResource(R.drawable.ic_finished);
        } else {
            myViewHolder.isFinished.setImageResource(R.drawable.ic_not_finished);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private int eventId;
        private TextView tv_Name;
        private TextView tv_Time;
        private TextView tv_Location;
        private ImageView isFinished;
        private String notes;


        public MyViewHolder(@NonNull final View itemView, Boolean canBeEdited) {
            super(itemView);

            isFinished = (ImageView) itemView.findViewById(R.id.isFinished);
            tv_Name = (TextView) itemView.findViewById(R.id.eventName);
            tv_Time = (TextView) itemView.findViewById(R.id.eventTime);
            tv_Location = (TextView) itemView.findViewById(R.id.position);

            if(canBeEdited) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent i = new Intent(itemView.getContext(), DetailsActivity.class);
                        i.putExtra("event_id", eventId);
                        itemView.getContext().startActivity(i);
                        return true;
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), EditNotesActivity.class);
                    i.putExtra("notes", notes);
                    i.putExtra("eventId", eventId);
                    itemView.getContext().startActivity(i);

                }
            });

        }
    }
}
