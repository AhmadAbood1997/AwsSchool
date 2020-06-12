package com.example.schoolapp.Views.Fragments;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.GroupAdapter;
import com.example.schoolapp.Adapters.InsertSubjectAdapter;
import com.example.schoolapp.Adapters.UsersAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Group;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.ProfilePersonActivity;
import com.example.schoolapp.Views.Activities.UploadVideoActivity;
import com.example.schoolapp.Views.dialog.DialogAddGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupFragment extends Fragment {


    private FragmentActivity myContext;


    private RecyclerView recycleGroups;
    private RecyclerView.LayoutManager layoutManagerGroups;
    private GroupAdapter groupAdapter;
    private List<Group> groups;

    DatabaseReference refCourse;


    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    AlertDialog dialog;

    public GroupFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();


        recycleGroups = view.findViewById(R.id.recycleGroups);
        recycleGroups.setHasFixedSize(true);
        layoutManagerGroups = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        groupAdapter = new GroupAdapter(getContext(), groups);

        recycleGroups.setLayoutManager(layoutManagerGroups);
        groups = new ArrayList<>();


        groupAdapter.setData(groups);


        recycleGroups.setAdapter(groupAdapter);


        return view;
    }

    private void RequestNewGroup() {
        DialogAddGroup dialogAddGroup = new DialogAddGroup();
        dialogAddGroup.show(myContext.getSupportFragmentManager(), "dialogGroups");
    }


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public void onStart() {
        super.onStart();
        ProfilePersonActivity.state = "";
        if (isNetworkConnected()) {
            viewAllFiles();
            dialog.dismiss();
        } else if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void viewAllFiles() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();

        FirebaseDatabase.getInstance().getReference("Groups")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FirebaseDatabase.getInstance().getReference("Groups")
                                    .child(snapshot.getKey()).child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    groups.clear();

                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        if (currentUserID.equals(snapshot1.getKey())) {

                                           FirebaseDatabase.getInstance().getReference("Groups")
                                                    .child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            groups.add(dataSnapshot.getValue(Group.class));
                                                            groupAdapter.setData(groups);
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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
