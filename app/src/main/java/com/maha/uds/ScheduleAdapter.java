package com.maha.uds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

     private Activity context;
     private List<Schedule> mScheduleList;


    public ScheduleAdapter(Activity context,List<Schedule> mScheduleList){
        super(context,R.layout.mother_schedule,mScheduleList);
        this.context = context;
        this.mScheduleList= mScheduleList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem= inflater.inflate(R.layout.mother_schedule,null,true);

        TextView dateTextView = listViewItem.findViewById(R.id.dateView);
        TextView dayTextView =listViewItem.findViewById(R.id.dayView);
        TextView timeTextView=listViewItem.findViewById(R.id.timeView);
        Schedule schedule = mScheduleList.get(position);

        dateTextView.setText(schedule.getDate());
        dayTextView.setText(schedule.getDay());
        timeTextView.setText(schedule.getTime());



        return listViewItem;
    }
}
