package com.example.schoolapp.Views.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Adapters.InsertMarksCourseAdapter;
import com.example.schoolapp.Adapters.InsertMarksSubjectAdapter;
import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
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

public class InsertMarksCourseActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference refCourse;


    DatabaseReference databaseReference;


    private RecyclerView recycleInsertNearlyTest;
    private RecyclerView.LayoutManager layoutManagerInsertCourse;
    private InsertMarksCourseAdapter insertMarksCourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_nearly_test);


        recycleInsertNearlyTest = (RecyclerView) findViewById(R.id.recycleInsertNearlyTest);
        recycleInsertNearlyTest.setHasFixedSize(true);
        layoutManagerInsertCourse = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        insertMarksCourseAdapter = new InsertMarksCourseAdapter(this, courses);

        recycleInsertNearlyTest.setLayoutManager(layoutManagerInsertCourse);
        courses = new ArrayList<>();

        insertMarksCourseAdapter.setData(courses);

        recycleInsertNearlyTest.setAdapter(insertMarksCourseAdapter);


        refCourse = database.getInstance().getReference().child("Cours");


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
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(InsertMarksSubjectAdapter.nameSubject).child("Cours");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Course course = postSnapShot.getValue(Course.class);
                    if (InsertMarksSubjectAdapter.nameSubject.equals(course.getNameSubject()))
                        courses.add(course);
                }

                insertMarksCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


}
