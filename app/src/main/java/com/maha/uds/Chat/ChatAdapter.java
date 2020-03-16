package com.maha.uds.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maha.uds.MainActivity;
import com.maha.uds.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    public static final String TAG = "Chat Adapter";

    private Context mContext;
    private List<MessageModel> mMessageList;
    private List<String> mKeys;

    private boolean isImageFitToScreen;




    public ChatAdapter(Context mContext, List<MessageModel> mMessageList, List<String> mKeys) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        this.mKeys = mKeys;
    }


    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;

        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.cardview_chat,parent,false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.MyViewHolder holder, final int position) {

        final MessageModel mMessageModel = mMessageList.get(position);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(mMessageModel.getCreationDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM hh:mm aaa");
        String dateString = formatter.format(mCalendar.getTime());

        if (mMessageModel.getSenderID().equals(ChatKeys.USER_ID)){
            holder.boxRL.setGravity(Gravity.END);
            holder.dateTxtV.setGravity(Gravity.END);
            holder.messageLL.setBackgroundResource(R.drawable.chat_bubble);
            holder.imageRL.setBackgroundResource(R.drawable.chat_bubble);
        }else {
            holder.dateTxtV.setGravity(Gravity.START);
            holder.boxRL.setGravity(Gravity.START);
            holder.messageLL.setBackgroundResource(R.drawable.chat_recive_bubble);
            holder.imageRL.setBackgroundResource(R.drawable.chat_recive_bubble);
        }



        if (mMessageModel.getMessageType().equals(ChatKeys.TEXT)){
            holder.messageLL.setVisibility(View.VISIBLE);
            holder.imageRL.setVisibility(View.GONE);



            holder.messageTxtV.setText(mMessageModel.getMessage());
            holder.dateTxtV.setText(dateString);

        }else if (mMessageModel.getMessageType().equals(ChatKeys.IMAGE)){
            holder.messageLL.setVisibility(View.GONE);
            holder.imageRL.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(mMessageModel.getMessage()).into(holder.imageView);
            holder.dateForImgTxtV.setText(dateString);

        }else {
            holder.messageLL.setVisibility(View.VISIBLE);
            holder.imageRL.setVisibility(View.GONE);

            holder.messageTxtV.setText("Unknown Type");
            holder.dateTxtV.setText(dateString);

        }
        Log.e("MessageModel",mMessageModel.toString());



        holder.imageRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Intent mIntent = new Intent(mContext,ImageActivity.class);
                    mIntent.putExtra("ImageURL",mMessageModel.getMessage());
                    mContext.startActivity(mIntent);

                }catch (Exception ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTxtV , dateTxtV,dateForImgTxtV ;
        private RelativeLayout boxRL;
        private RelativeLayout imageRL;
        private LinearLayout messageLL;
        private ImageView imageView;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTxtV =  itemView.findViewById(R.id.messageTxtV);
            dateTxtV = itemView.findViewById(R.id.dateTxtV);
            dateForImgTxtV = itemView.findViewById(R.id.dateForImgTxtV);
            boxRL = itemView.findViewById(R.id.boxRL);
            imageRL = itemView.findViewById(R.id.imageRL);
            messageLL = itemView.findViewById(R.id.messageLL);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}

