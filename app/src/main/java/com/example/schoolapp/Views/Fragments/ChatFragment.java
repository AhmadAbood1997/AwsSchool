package com.example.schoolapp.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolapp.Adapters.UsersAdapter;
import com.example.schoolapp.Models.Entities.Contact;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.ChatActivity;
import com.example.schoolapp.Views.Activities.FindFriendsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private RecyclerView recycleChat;

    private FloatingActionButton btnFindFriend;

    private DatabaseReference ChatRef;
    private DatabaseReference UserRef;

    private FirebaseAuth firebaseAuth;
    private String currentUserID;


    private String retImage = "";

    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        btnFindFriend = view.findViewById(R.id.btnFindFriend);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        ChatRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        btnFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FindFriendsActivity.class);
                startActivity(intent);
            }
        });


        recycleChat = view.findViewById(R.id.recycleChat);
        recycleChat.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(ChatRef, Contact.class)
                        .build();

        FirebaseRecyclerAdapter<Contact, ChatViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contact, ChatViewHolder>(options)  {
                    @Override
                    protected void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int position, @NonNull Contact contact) {
                        final String userid = getRef(position).getKey();

                        UserRef.child(userid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.hasChild("image")) {
                                        retImage = dataSnapshot.child("image").getValue().toString();
                                        if (!retImage.equals(""))
                                            Picasso.get().load(retImage).into(chatViewHolder.imgChatFrgProfile);
                                    }


                                    final String retName = dataSnapshot.child("name").getValue().toString();

                                    chatViewHolder.txtChatName.setText(retName);




                                    if (dataSnapshot.child("userState").hasChild("state")) {
                                        String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                        String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                        String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                        if (state.equals("online")) {
                                            chatViewHolder.txtChatLastSeen.setText("online");
                                        } else if (state.equals("offline")) {
                                            chatViewHolder.txtChatLastSeen.setText("Last Seen: "+date+" "+ time );
                                        }

                                    } else {
                                        chatViewHolder.txtChatLastSeen.setText("offline");
                                    }


                                    chatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                            intent.putExtra("userid", userid);
                                            intent.putExtra("name", retName);
                                            intent.putExtra("image", retImage);



                                            startActivity(intent);
                                        }
                                    });

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_chat, viewGroup, false);
                        return new ChatViewHolder(view);
                    }
                };

        recycleChat.setAdapter(adapter);
        adapter.startListening();

    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgChatFrgProfile;
        TextView txtChatName;
        TextView txtChatLastSeen;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            imgChatFrgProfile = itemView.findViewById(R.id.imgChatFrgProfile);
            txtChatName = itemView.findViewById(R.id.txtChatName);
            txtChatLastSeen = itemView.findViewById(R.id.txtChatLastSeen);


        }
    }


}
