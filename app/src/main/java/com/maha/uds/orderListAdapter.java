package com.maha.uds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maha.uds.Model.OrderModel;

import java.util.List;

public class orderListAdapter extends RecyclerView.Adapter<orderListAdapter.ViewHolder> {

    private List<OrderModel> orderList;
    private Context mContext;

    public orderListAdapter(List<OrderModel> orderList, Context context) {
        this.orderList = orderList;
        mContext = context;

    }
    @NonNull
    @Override
    public orderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_cardview, parent, false);
        return new orderListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull orderListAdapter.ViewHolder holder, int position) {
        final OrderModel orderModel = orderList.get(position);
        holder.Date.setText(orderModel.getOrderDate());
        holder.orderStatus.setText(orderModel.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Date;
        TextView orderStatus;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            mCardView = itemView.findViewById(R.id.cardView);


        }
    }
}
