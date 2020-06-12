package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertNearlyTestSubjectAdapter;
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

public class UploadPdfSubjectActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference refSubject;
    DatabaseReference databaseReference;


    private RecyclerView recUploadPdfSubject;
    private RecyclerView.LayoutManager layoutManagerUploadPdfSubject;
    private UploadPdfSubjectAdapter uploadPdfSubjectAdapter;
    private List<Subject> subjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf_subject);


        refSubject = database.getInstance().getReference().child("Subject");


        recUploadPdfSubject = (RecyclerView) findViewById(R.id.recUploadPdfSubject);
        recUploadPdfSubject.setHasFixedSize(true);
        layoutManagerUploadPdfSubject = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        uploadPdfSubjectAdapter = new UploadPdfSubjectAdapter(this, subjects);

        recUploadPdfSubject.setLayoutManager(layoutManagerUploadPdfSubject);
        subjects = new ArrayList<>();


        uploadPdfSubjectAdapter.setData(subjects);


        recUploadPdfSubject.setAdapter(uploadPdfSubjectAdapter);






    }

    public  boolean isNetworkConnected() {
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

                uploadPdfSubjectAdapter.setData(subjects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


}
