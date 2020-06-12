package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.RetriveMp3CourseAdapter;
import com.example.schoolapp.Adapters.RetriveMp3SubjectAdapter;
import com.example.schoolapp.Adapters.UploadMp3CourseAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.RetriveMp3SubjectAdapter.nameSubjectRetriveMp3;
import static com.example.schoolapp.Adapters.UploadMp3SubjectAdapter.nameSubjectUploadMp3;

public class RetriveMp3CoursActivity extends AppCompatActivity {



    DatabaseReference refCourse;





    private RecyclerView recRetriveMp3Cours;
    private RecyclerView.LayoutManager layoutManagerUploadPdfCours;
    private RetriveMp3CourseAdapter retriveMp3CourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_mp3_cours);



        recRetriveMp3Cours = (RecyclerView) findViewById(R.id.recRetriveMp3Cours);
        recRetriveMp3Cours.setHasFixedSize(true);
        layoutManagerUploadPdfCours = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retriveMp3CourseAdapter = new RetriveMp3CourseAdapter(this, courses);

        recRetriveMp3Cours.setLayoutManager(layoutManagerUploadPdfCours);
        courses = new ArrayList<>();

        retriveMp3CourseAdapter.setData(courses);

        recRetriveMp3Cours.setAdapter(retriveMp3CourseAdapter);


        refCourse = FirebaseDatabase.getInstance().getReference("Subject").child(RetriveMp3SubjectAdapter.nameSubjectRetriveMp3).child("Cours");




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
                    if ( nameSubjectRetriveMp3.equals(course.getNameSubject()))
                        courses.add(course);
                }

                retriveMp3CourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

}
