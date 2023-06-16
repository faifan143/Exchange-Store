package com.faifan143.exchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.activities.InChatScreen;
import com.faifan143.exchange.models.ContactModel;

import java.util.List;

public class ChatScreenAdapter extends RecyclerView.Adapter<MyChatScreenViewHolder>  {

    private Context context;
    private List<ContactModel> dataList;

    public ChatScreenAdapter(Context context, List<ContactModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyChatScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        return new MyChatScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatScreenViewHolder holder, int position) {
        ContactModel contactModel = dataList.get(position);


        holder.contactEmailView.setText(contactModel.getEmail());
        holder.contactNumberView.setText(contactModel.getNumber());
        holder.contactNameView.setText(contactModel.getName());


        holder.contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InChatScreen.class);

                // Get the sender email, receiver email, and message text
                String number = dataList.get(holder.getAdapterPosition()).getNumber();
                String name = dataList.get(holder.getAdapterPosition()).getName();
                String email = dataList.get(holder.getAdapterPosition()).getEmail();
                String fcmToken = contactModel.getFCMToken();

                // Add the sender email, receiver email, and message text as extras to the intent
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("number", number);
                intent.putExtra("fcmToken", fcmToken);
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.size();
        } else {
            return 0; // return 1 to show "فارغ" in the list
        }
    }
}

class MyChatScreenViewHolder extends RecyclerView.ViewHolder{
    TextView contactEmailView, contactNumberView ,contactNameView ;
    CardView contactCard;
    public MyChatScreenViewHolder(@NonNull View itemView) {
        super(itemView);
        contactEmailView = itemView.findViewById(R.id.chat_screen_contact_email);
        contactNumberView = itemView.findViewById(R.id.chat_screen_contact_number);
        contactNameView = itemView.findViewById(R.id.chat_screen_contact_name);
        contactCard = itemView.findViewById(R.id.contact_card);
    }
}