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

import com.example.schoolapp.Models.Entities.Contact;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.ProfilePersonActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactFragment extends Fragment {



    private DatabaseReference ContactRef;
    private DatabaseReference UserRef;


    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    private RecyclerView recycleFrgContact;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        recycleFrgContact = view.findViewById(R.id.recycleFrgContact);
        recycleFrgContact.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options
                = new FirebaseRecyclerOptions.Builder<Contact>()
                .setQuery(ContactRef, Contact.class)
                .build();

        FirebaseRecyclerAdapter<Contact, ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contact, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ContactsViewHolder contactsViewHolder, int position, @NonNull Contact contact) {


                String userID = getRef(position).getKey();

                UserRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                       if(dataSnapshot.exists())

                       {
                           if (dataSnapshot.child("userState").hasChild("state")) {
                               String state = dataSnapshot.child("userState").child("state").getValue().toString();

                               if (state.equals("online")) {
                                   contactsViewHolder.txtContactOnline.setVisibility(View.VISIBLE);
                               } else if (state.equals("offline")) {
                                   contactsViewHolder.txtContactOnline.setVisibility(View.GONE);
                               }

                           } else {
                               contactsViewHolder.txtContactOnline.setVisibility(View.GONE);
                           }

                           if (dataSnapshot.hasChild("image") && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status")) {



                               String profileImage = dataSnapshot.child("image").getValue().toString();
                               String profileName = dataSnapshot.child("name").getValue().toString();
                               String profileStatus = dataSnapshot.child("status").getValue().toString();
                               if (!profileImage.equals(""))
                                   Picasso.get().load(profileImage).into(contactsViewHolder.imgProfileFrgContact);
                               contactsViewHolder.txtFrgContactName.setText(profileName);
                               contactsViewHolder.txtFrgContactStatus.setText(profileStatus);


                               contactsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       ProfilePersonActivity.state = "Friend";
                                       Intent intent = new Intent(getContext(), ProfilePersonActivity.class);
                                       intent.putExtra("name", profileName);
                                       intent.putExtra("status", profileStatus);
                                       intent.putExtra("image", profileImage);
                                       intent.putExtra("userid", userID);
                                       startActivity(intent);
                                   }
                               });


                           }

                           else if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status")) {
                               String profileName = dataSnapshot.child("name").getValue().toString();
                               String profileStatus = dataSnapshot.child("status").getValue().toString();

                               contactsViewHolder.txtFrgContactName.setText(profileName);
                               contactsViewHolder.txtFrgContactStatus.setText(profileStatus);

                               contactsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       ProfilePersonActivity.state = "Friend";
                                       Intent intent = new Intent(getContext(), ProfilePersonActivity.class);
                                       intent.putExtra("name", profileName);
                                       intent.putExtra("status", profileStatus);
                                       intent.putExtra("image", "");
                                       intent.putExtra("userid", userID);
                                       startActivity(intent);
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
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_friend, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        recycleFrgContact.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfileFrgContact;
        TextView txtFrgContactName;
        TextView txtFrgContactStatus;
        TextView  txtContactOnline;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfileFrgContact = itemView.findViewById(R.id.imgProfileFrgContact);
            txtFrgContactName = itemView.findViewById(R.id.txtFrgContactName);
            txtFrgContactStatus = itemView.findViewById(R.id.txtFrgContactStatus);
            txtContactOnline = itemView.findViewById(R.id.txtContactOnline);

        }
    }


}
