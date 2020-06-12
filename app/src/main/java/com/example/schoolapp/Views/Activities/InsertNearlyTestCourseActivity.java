package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.InsertNearlyTestSubjectAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.InsertNearlyTestSubjectAdapter.nameSubject;
import static com.example.schoolapp.Views.Activities.InsertCoursesActivity.subject;

public class InsertNearlyTestCourseActivity extends AppCompatActivity {

    DatabaseReference refCourse;


    private RecyclerView recycleInsertNearlyTest;
    private RecyclerView.LayoutManager layoutManagerInsertCourse;
    private InsertNearlyTestCourseAdapter insertNearlyTestCourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_nearly_test);


        recycleInsertNearlyTest = (RecyclerView) findViewById(R.id.recycleInsertNearlyTest);
        recycleInsertNearlyTest.setHasFixedSize(true);
        layoutManagerInsertCourse = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        insertNearlyTestCourseAdapter = new InsertNearlyTestCourseAdapter(this, courses);

        recycleInsertNearlyTest.setLayoutManager(layoutManagerInsertCourse);
        courses = new ArrayList<>();

        insertNearlyTestCourseAdapter.setData(courses);

        recycleInsertNearlyTest.setAdapter(insertNearlyTestCourseAdapter);


        refCourse = FirebaseDatabase.getInstance().getReference("Subject").child(InsertNearlyTestSubjectAdapter.nameSubject).child("Cours");


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
                    if ( nameSubject.equals(course.getNameSubject()))
                        courses.add(course);
                }

                insertNearlyTestCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


}
