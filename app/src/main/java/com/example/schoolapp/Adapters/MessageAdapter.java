package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Message;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderMessage;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<ViewHolderMessage> {
    private Context context;
    private List<Message> messages;
    public static int Pos;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }


    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_group, parent, false);
        return new ViewHolderMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage viewHolderMessage, int position) {

        Pos = position;
        final Message message = messages.get(position);

        viewHolderMessage.getTxtMessageCurrentDate().setText(message.getDate());
        viewHolderMessage.getTxtMessageCurrentTime().setText(message.getTime());
        viewHolderMessage.getTxtMessageMessageCurentUser().setText(message.getMessage());
        viewHolderMessage.getTxtMessageNameCurrentUser().setText(message.getName());


    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    public void setData(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

}
