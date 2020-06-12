package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.UploadMp3CourseAdapter;
import com.example.schoolapp.Adapters.UploadMp3SubjectAdapter;
import com.example.schoolapp.Adapters.UploadPdfCourseAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.UploadMp3SubjectAdapter.nameSubjectUploadMp3;
import static com.example.schoolapp.Adapters.UploadPdfSubjectAdapter.nameSubjectUploadPdf;

public class UploadMp3CoursActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference refCourse;





    private RecyclerView recUploadMp3Cours;
    private RecyclerView.LayoutManager layoutManagerUploadPdfCours;
    private UploadMp3CourseAdapter uploadMp3CourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_mp3_cours);



        recUploadMp3Cours = (RecyclerView) findViewById(R.id.recUploadMp3Cours);
        recUploadMp3Cours.setHasFixedSize(true);
        layoutManagerUploadPdfCours = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        uploadMp3CourseAdapter = new UploadMp3CourseAdapter(this, courses);

        recUploadMp3Cours.setLayoutManager(layoutManagerUploadPdfCours);
        courses = new ArrayList<>();

        uploadMp3CourseAdapter.setData(courses);

        recUploadMp3Cours.setAdapter(uploadMp3CourseAdapter);


        refCourse =  FirebaseDatabase.getInstance().getReference("Subject")
                .child(UploadMp3SubjectAdapter.nameSubjectUploadMp3)
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
                    if ( nameSubjectUploadMp3.equals(course.getNameSubject()))
                        courses.add(course);
                }

                uploadMp3CourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

}
