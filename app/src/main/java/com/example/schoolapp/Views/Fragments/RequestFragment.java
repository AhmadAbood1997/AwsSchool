package com.example.schoolapp.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.schoolapp.Models.Entities.Contact;
import com.example.schoolapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFragment extends Fragment {

    private RecyclerView recycleFrgRequest;


    private DatabaseReference ChatRequestRef;
    private DatabaseReference UserRef;
    private DatabaseReference ContactRef;


    private FirebaseAuth firebaseAuth;

    private String currentUserID;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        recycleFrgRequest = view.findViewById(R.id.recycleFrgRequest);
        recycleFrgRequest.setLayoutManager(new LinearLayoutManager(getContext()));


        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getUid();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(ChatRequestRef.child(currentUserID), Contact.class)
                        .build();

        FirebaseRecyclerAdapter<Contact, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contact, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestsViewHolder requestsViewHolder, int position, @NonNull Contact contact) {

                        final String userid = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();


                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    String type = dataSnapshot.getValue().toString();
                                    if (type.equals("received")) {
                                        UserRef.child(userid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("image")) {

                                                    final String requestProfileImage = dataSnapshot.child("image").getValue().toString();

                                                    if (!requestProfileImage.equals(""))
                                                    Picasso.get().load(requestProfileImage).into(requestsViewHolder.imgProfileFrgRequest);
                                                }
                                                final String requestUserName = dataSnapshot.child("name").getValue().toString();
                                                final String requestUserStatus = dataSnapshot.child("status").getValue().toString();

                                                requestsViewHolder.txtFrgRequestName.setText(requestUserName);
                                                requestsViewHolder.txtFrgRequestStatus.setText("Wants to connect with you");
                                                requestsViewHolder.btnAcceptRequest.setVisibility(View.VISIBLE);
                                                requestsViewHolder.btnCancelRequest.setVisibility(View.VISIBLE);


                                                requestsViewHolder.btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        ContactRef.child(currentUserID)
                                                                .child(userid).child("Contacts")
                                                                .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    ContactRef.child(userid).child(currentUserID).child("Contacts")
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                ChatRequestRef.child(currentUserID)
                                                                                        .child(userid).removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if(task.isSuccessful())
                                                                                                {
                                                                                                    ChatRequestRef.child(userid)
                                                                                                            .child(currentUserID)
                                                                                                            .removeValue();
                                                                                                }

                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                                }

                                                            }
                                                        });
                                                    }
                                                });





                                                requestsViewHolder.btnCancelRequest.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {






                                                        ChatRequestRef.child(currentUserID)
                                                                .child(userid).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            ChatRequestRef.child(userid)
                                                                                    .child(currentUserID)
                                                                                    .removeValue();
                                                                        }

                                                                    }
                                                                });








                                                    }
                                                });













                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                    @NonNull
                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_request, viewGroup, false);
                        RequestsViewHolder holder = new RequestsViewHolder(view);
                        return holder;
                    }
                };

        recycleFrgRequest.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfileFrgRequest;
        TextView txtFrgRequestName;
        TextView txtFrgRequestStatus;
        Button btnAcceptRequest;
        Button btnCancelRequest;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfileFrgRequest = itemView.findViewById(R.id.imgProfileFrgRequest);
            txtFrgRequestName = itemView.findViewById(R.id.txtFrgRequestName);
            txtFrgRequestStatus = itemView.findViewById(R.id.txtFrgRequestStatus);
            btnAcceptRequest = itemView.findViewById(R.id.btnAcceptRequest);
            btnCancelRequest = itemView.findViewById(R.id.btnCancelRequest);

        }
    }


}
