package com.example.pv239_android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pv239_android.model.Details;
import com.example.pv239_android.model.Event;
import com.example.pv239_android.model.EventItem;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<EventItem> mData;

    public RecyclerViewAdapter(Context mContext, List<EventItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_event, viewGroup, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_Name.setText(mData.get(i).getmName());
        myViewHolder.tv_Time.setText(mData.get(i).getmTime() );
        myViewHolder.tv_Location.setText(mData.get(i).getmPosition());
        myViewHolder.notes = mData.get(i).getmNotes();
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
        private ImageButton editNotes;
        private String notes;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            editNotes = (ImageButton) itemView.findViewById(R.id.editNotes);
            tv_Name = (TextView) itemView.findViewById(R.id.eventName);
            tv_Time = (TextView) itemView.findViewById(R.id.eventTime);
            tv_Location = (TextView) itemView.findViewById(R.id.position);

            editNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), EditNotesActivity.class);
                    i.putExtra("notes", notes);
                    itemView.getContext().startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent i = new Intent(itemView.getContext(), Details.class);
                    i.putExtra("event_id", eventId);
                    itemView.getContext().startActivity(i);
                    return true;
                }
            });
        }


    }


}
