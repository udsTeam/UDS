package com.maha.uds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.ScheduleModel;

import java.util.List;

public class WorkScheduleAdapter extends RecyclerView.Adapter<WorkScheduleAdapter.ViewHolder> {

    private List<ScheduleModel> scheduleList;
    private List<String> keyList;
    private Context mContext;


    public WorkScheduleAdapter(List<ScheduleModel> scheduleList, List<String> keyList, Context context) {
        this.scheduleList = scheduleList;
        this.keyList = keyList;
        mContext = context;

    }

    @NonNull
    @Override
    public WorkScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_schedule_cardview, parent, false);
        return new WorkScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkScheduleAdapter.ViewHolder holder, final int position) {

        final ScheduleModel scheduleModel = scheduleList.get(position);
        final String kay = keyList.get(position);
        holder.Day.setText(scheduleModel.getDay());
        holder.Date.setText(scheduleModel.getDate());
        holder.Time.setText(scheduleModel.getTime());

        FirebaseDatabase.getInstance().getReference("orders")
                .child(BabysitterHome.mOrderKey).child("scheduleList").child(kay).orderByChild("time").equalTo("DONE!")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scheduleList.clear();
                                    FirebaseDatabase.getInstance().getReference("orders")
                                            .child(BabysitterHome.mOrderKey).child("scheduleList")
                                            .child(kay).child("time").setValue("DONE!");
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }



    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Day;
        TextView Date;
        TextView Time;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Day = itemView.findViewById(R.id.dayView);
            Date = itemView.findViewById(R.id.dateView);
            Time = itemView.findViewById(R.id.timeView);
            mCardView = itemView.findViewById(R.id.cardView);


        }

    }

}

