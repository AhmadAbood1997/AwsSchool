package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.RetrivePdfCourseAdapter;
import com.example.schoolapp.Adapters.RetrivePdfSubjectAdapter;
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

import static com.example.schoolapp.Adapters.RetrivePdfSubjectAdapter.nameSubjectRetrivePdf;
import static com.example.schoolapp.Adapters.UploadPdfSubjectAdapter.nameSubjectUploadPdf;

public class RetrivePdfCoursActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference refCourse;


    private RecyclerView recRetrivePdfCours;
    private RecyclerView.LayoutManager layoutManagerRetrivePdfCours;
    private RetrivePdfCourseAdapter retrivePdfCourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_pdf_cours);



        recRetrivePdfCours = (RecyclerView) findViewById(R.id.recRetrivePdfCours);
        recRetrivePdfCours.setHasFixedSize(true);
        layoutManagerRetrivePdfCours = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retrivePdfCourseAdapter = new RetrivePdfCourseAdapter(this, courses);

        recRetrivePdfCours.setLayoutManager(layoutManagerRetrivePdfCours);
        courses = new ArrayList<>();

        retrivePdfCourseAdapter.setData(courses);

        recRetrivePdfCours.setAdapter(retrivePdfCourseAdapter);


        refCourse = database.getInstance().getReference().child("Subject").child(nameSubjectRetrivePdf).child("Cours");




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
                    if ( nameSubjectRetrivePdf.equals(course.getNameSubject()))
                        courses.add(course);
                }

                retrivePdfCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

}
