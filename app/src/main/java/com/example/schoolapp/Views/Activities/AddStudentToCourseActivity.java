package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolapp.Adapters.AddStudentToCourseAdapter;
import com.example.schoolapp.Adapters.InsertCourseAdapter;
import com.example.schoolapp.Adapters.SubjectAdapter;
import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.dialog.TimePickerFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Views.Activities.InsertCoursesActivity.subject;

public class AddStudentToCourseActivity extends AppCompatActivity {


    DatabaseReference refCourse;



    private RecyclerView recycleAddStudentToCourses;
    private RecyclerView.LayoutManager layoutManagerInsertCourse;
    private AddStudentToCourseAdapter addStudentToCourseAdapter;
    private List<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_to_course);


        recycleAddStudentToCourses = (RecyclerView) findViewById(R.id.recycleAddStudentToCourses);
        recycleAddStudentToCourses.setHasFixedSize(true);
        layoutManagerInsertCourse = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        addStudentToCourseAdapter = new AddStudentToCourseAdapter(this, courses);

        recycleAddStudentToCourses.setLayoutManager(layoutManagerInsertCourse);
        courses = new ArrayList<>();

        addStudentToCourseAdapter.setData(courses);

        recycleAddStudentToCourses.setAdapter(addStudentToCourseAdapter);


        refCourse = FirebaseDatabase.getInstance().getReference().child("Subject").child(SubjectAdapter.nameSubject).child("Cours");


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
                    if (SubjectAdapter.nameSubject.equals(course.getNameSubject()))
                        courses.add(course);
                }

                addStudentToCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


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


}
