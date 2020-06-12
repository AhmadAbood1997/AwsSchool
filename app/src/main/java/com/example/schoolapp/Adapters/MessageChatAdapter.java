package com.example.schoolapp.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.MessageChat;
import com.example.schoolapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MessageChatViewHolder> {

    private List<MessageChat> messageChats;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;


    public MessageChatAdapter(List<MessageChat> messageChats) {
        this.messageChats = messageChats;
    }


    public class MessageChatViewHolder extends RecyclerView.ViewHolder {
        public TextView txtReceiveMessage;
        public TextView txtSendMessage;
        public CircleImageView imgProfileMessageChat;

        public MessageChatViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReceiveMessage = itemView.findViewById(R.id.txtReceiveMessage);
            txtSendMessage = itemView.findViewById(R.id.txtSendMessage);
            imgProfileMessageChat = itemView.findViewById(R.id.imgProfileMessageChat);


        }
    }


    @NonNull
    @Override
    public MessageChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_message_chat, viewGroup, false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new MessageChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageChatViewHolder messageChatViewHolder, int position) {
        String messageSenderID = firebaseAuth.getCurrentUser().getUid();
        MessageChat messageChat = messageChats.get(position);

        String from = messageChat.getFrom();
        String type = messageChat.getType();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(from);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();
                    if(!receiverImage.equals(""))
                    Picasso.get().load(receiverImage).placeholder(R.drawable.ic_interface).into(messageChatViewHolder.imgProfileMessageChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (type.equals("text")) {
            messageChatViewHolder.txtReceiveMessage.setVisibility(View.GONE);
            messageChatViewHolder.imgProfileMessageChat.setVisibility(View.GONE);
            messageChatViewHolder.txtSendMessage.setVisibility(View.GONE);


            if (from.equals(messageSenderID)) {


                messageChatViewHolder.txtSendMessage.setVisibility(View.VISIBLE);

                messageChatViewHolder.txtSendMessage.setText(messageChat.getMessage());
            }


            else {

                messageChatViewHolder.imgProfileMessageChat.setVisibility(View.VISIBLE);
                messageChatViewHolder.txtReceiveMessage.setVisibility(View.VISIBLE);

                messageChatViewHolder.txtReceiveMessage.setText(messageChat.getMessage());
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageChats.size();
    }


}
