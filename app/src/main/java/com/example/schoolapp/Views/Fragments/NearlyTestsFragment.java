package com.example.schoolapp.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolapp.Adapters.RetriveNearlyTestSubjectAdapter;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NearlyTestsFragment extends Fragment {


    private RecyclerView recycleTestSubjectNearlyTests;
    private RecyclerView.LayoutManager layoutManagerTestSubjectNearlyTests;
    private RetriveNearlyTestSubjectAdapter retriveNearlyTestSubjectAdapter;
    private List<Subject> subjects;
    private DatabaseReference databaseReference;



    public NearlyTestsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearly_test, container, false);

        recycleTestSubjectNearlyTests = (RecyclerView) view.findViewById(R.id.recycleTestSubjectNearlyTests);
        recycleTestSubjectNearlyTests.setHasFixedSize(true);
        layoutManagerTestSubjectNearlyTests = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        retriveNearlyTestSubjectAdapter = new RetriveNearlyTestSubjectAdapter(getContext(), subjects);

        recycleTestSubjectNearlyTests.setLayoutManager(layoutManagerTestSubjectNearlyTests);
        subjects = new ArrayList<>();

        viewAllFiles();


        return view;
    }


    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Subject");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Subject subject = postSnapShot.getValue(Subject.class);
                    subjects.add(subject);

                }

                retriveNearlyTestSubjectAdapter.setData(subjects);


                recycleTestSubjectNearlyTests.setAdapter(retriveNearlyTestSubjectAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

}
