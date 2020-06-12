package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.RetrivePdfSubjectAdapter;
import com.example.schoolapp.Adapters.UploadPdfSubjectAdapter;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrivePdfSubjectActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference refSubject;
    DatabaseReference databaseReference;


    private RecyclerView recRetrivePdfSubject;
    private RecyclerView.LayoutManager layoutManagerRetrivePdfSubject;
    private RetrivePdfSubjectAdapter retrivePdfSubjectAdapter;
    private List<Subject> subjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_pdf_subject);

        ProfilePersonActivity.state = "RetriveLectures";


        refSubject = database.getInstance().getReference().child("Subject");


        recRetrivePdfSubject = (RecyclerView) findViewById(R.id.recRetrivePdfSubject);
        recRetrivePdfSubject.setHasFixedSize(true);
        layoutManagerRetrivePdfSubject = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retrivePdfSubjectAdapter = new RetrivePdfSubjectAdapter(this, subjects);

        recRetrivePdfSubject.setLayoutManager(layoutManagerRetrivePdfSubject);
        subjects = new ArrayList<>();


        retrivePdfSubjectAdapter.setData(subjects);


        recRetrivePdfSubject.setAdapter(retrivePdfSubjectAdapter);


    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected())
            viewAllFiles();

        else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
        }
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

                retrivePdfSubjectAdapter.setData(subjects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


}
