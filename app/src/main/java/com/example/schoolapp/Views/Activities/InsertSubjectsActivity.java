package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertCourseAdapter;
import com.example.schoolapp.Adapters.InsertSubjectAdapter;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsertSubjectsActivity extends AppCompatActivity {






    FirebaseDatabase database;
    DatabaseReference refSubject;
    DatabaseReference databaseReference;


    private RecyclerView recycleInsertSubjects;
    private RecyclerView.LayoutManager layoutManagerInsertSubject;
    private InsertSubjectAdapter insertSubjectAdapter;
    private List<Subject> subjects;

    private FloatingActionButton fabOpenBottomSheetSubject;

    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottomSheetInsertSubject;



    private Button btnAddSubject;


    private EditText edtBottomSheetSubjectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_subjects);


        btnAddSubject = findViewById(R.id.btnAddSubject);


        edtBottomSheetSubjectName = findViewById(R.id.edtSubjectBottomSheetSubjectName);


        recycleInsertSubjects = (RecyclerView) findViewById(R.id.recycleInsertSubjects);
        recycleInsertSubjects.setHasFixedSize(true);
        layoutManagerInsertSubject = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        insertSubjectAdapter = new InsertSubjectAdapter(this, subjects);

        recycleInsertSubjects.setLayoutManager(layoutManagerInsertSubject);
        subjects = new ArrayList<>();


        insertSubjectAdapter.setData(subjects);


        recycleInsertSubjects.setAdapter(insertSubjectAdapter);


        bottomSheetInsertSubject = (RelativeLayout) findViewById(R.id.bottomSheetInsertSubject);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInsertSubject);


        fabOpenBottomSheetSubject = findViewById(R.id.fabOpenBottomSheetSubject);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetSubject.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    clear();
                    fabOpenBottomSheetSubject.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        fabOpenBottomSheetSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        refSubject = database.getInstance().getReference().child("Subject");


        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String SubjectName = edtBottomSheetSubjectName.getText().toString().trim();


                if (SubjectName.isEmpty()) {
                    edtBottomSheetSubjectName.setError("SubjectName Requierd");
                    edtBottomSheetSubjectName.requestFocus();
                    return;
                }


                if (isNetworkConnected()) {
                    Subject subject = new Subject(edtBottomSheetSubjectName.getText().toString());


                    refSubject.child(subject.getName()).setValue(subject);


                    clear();


                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                } else {
                    Toast.makeText(InsertSubjectsActivity.this, "No connect with internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void clear() {
        edtBottomSheetSubjectName.setText("");
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

                insertSubjectAdapter.setData(subjects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
