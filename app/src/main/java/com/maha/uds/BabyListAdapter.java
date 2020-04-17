package com.maha.uds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maha.uds.Model.BabyModel;

import java.util.List;

public class BabyListAdapter extends RecyclerView.Adapter<BabyListAdapter.ViewHolder> {

    private List<BabyModel> babyList;
    private List<String> orderKeyList;
    private Context mContext;



    public BabyListAdapter( List<BabyModel> babyList,List<String> orderKeyList, Context context){
        this.babyList = babyList;
        this.orderKeyList = orderKeyList;
        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BabyListAdapter.ViewHolder holder, final int position) {
        final BabyModel babyModel = babyList.get(position);
        holder.name.setText(babyModel.getName());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,BabyInformation.class);
                intent.putExtra("name",babyModel.getName());
                intent.putExtra("age",babyModel.getAge());
                intent.putExtra("notes",babyModel.getBabyNotes());
                intent.putExtra("gender",babyModel.getGender());
                intent.putExtra("OrderID",orderKeyList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return babyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            name=itemView.findViewById(R.id.nameView);
            mCardView = itemView.findViewById(R.id.cardView);




        }

    }

}
