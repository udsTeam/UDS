package com.maha.uds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maha.uds.Model.ScheduleModel;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<ScheduleModel> {

     private Activity context;
     private List<ScheduleModel> mScheduleList;


    public ScheduleAdapter(Activity context,List<ScheduleModel> schedulelList){
        super(context,R.layout.child_schedule,schedulelList);
        this.context = context;
        this.mScheduleList= schedulelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem= inflater.inflate(R.layout.schedule_list_view,null,false);

        TextView dayView = listViewItem.findViewById(R.id.dayView);
        TextView dateView = listViewItem.findViewById(R.id.dateView);
        TextView timeView = listViewItem.findViewById(R.id.timeView);


        ScheduleModel scheduleModel = mScheduleList.get(position);
        dayView.setText(scheduleModel.getDay());
        dateView.setText(scheduleModel.getDate());
        timeView.setText(scheduleModel.getTime());

        return listViewItem;
    }
}
