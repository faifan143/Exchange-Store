package com.faifan143.exchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageViewHolder> {

    List<MessageModel> messages = new ArrayList<MessageModel>();
    private Context context;
    private String myEmail;
    public MyMessageAdapter(Context context , List<MessageModel> messages ,String myEmail) {
        this.context = context;
        this.messages=messages;
        this.myEmail=myEmail;
    }

    @NonNull
    @Override
    public MyMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // sent message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
        } else { // received message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_message, parent, false);
        }
        return new MyMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageViewHolder holder, int position) {
        MessageModel message = messages.get(position);

        holder.messageBody.setText(message.getMessageBody());
    }

    @Override
    public int getItemCount() {
        if (messages != null && !messages.isEmpty()) {
            return messages.size();
        } else {
            return 0; // return 1 to show "فارغ" in the list
        }
    }

    public void searchDataList(ArrayList<MessageModel> searchList){
        messages = searchList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messages.get(position);
        if (message.getReceiverEmail().equals(myEmail)) {
            return 1; // sent message
        } else {
            return 0; // received message
        }
    }
}


class MyMessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageBody;

    public MyMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageBody = itemView.findViewById(R.id.message_body);
    }
}
