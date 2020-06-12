package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.RetriveVideoCourseAdapter;
import com.example.schoolapp.Adapters.RetriveVideoSubjectAdapter;
import com.example.schoolapp.Adapters.UploadVideoCourseAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.RetriveVideoSubjectAdapter.nameSubjectRetriveVideo;
import static com.example.schoolapp.Adapters.UploadVideoSubjectAdapter.nameSubjectUploadVideo;

public class RetriveVideoCoursActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference refCourse;


    DatabaseReference databaseReference;


    private RecyclerView recRetriveVideoCours;
    private RecyclerView.LayoutManager layoutManagerUploadPdfCours;
    private RetriveVideoCourseAdapter retriveVideoCourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_video_cours);



        recRetriveVideoCours = (RecyclerView) findViewById(R.id.recRetriveVideoCours);
        recRetriveVideoCours.setHasFixedSize(true);
        layoutManagerUploadPdfCours = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retriveVideoCourseAdapter = new RetriveVideoCourseAdapter(this, courses);

        recRetriveVideoCours.setLayoutManager(layoutManagerUploadPdfCours);
        courses = new ArrayList<>();

        retriveVideoCourseAdapter.setData(courses);

        recRetriveVideoCours.setAdapter(retriveVideoCourseAdapter);


        refCourse = FirebaseDatabase.getInstance().getReference("Subject").child(RetriveVideoSubjectAdapter.nameSubjectRetriveVideo)
                .child("Cours");




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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void viewAllFiles() {
        refCourse.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Course course = postSnapShot.getValue(Course.class);
                    if ( nameSubjectRetriveVideo.equals(course.getNameSubject()))
                        courses.add(course);
                }

                retriveVideoCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

}
