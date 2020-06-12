package com.example.schoolapp.Views.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.CourseAdapter;
import com.example.schoolapp.Adapters.SubjectAdapter;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {


    DatabaseReference refSubject;


    private RecyclerView recyclerSubject;
    private RecyclerView.LayoutManager layoutManagerSubject;
    private SubjectAdapter subjectAdapter;
    public static List<Subject> subjects;


    private RecyclerView recyclerAppointments;
    private RecyclerView.LayoutManager layoutManagerAppointments;
    private CourseAdapter courseAdapter;
    private List<Course> courses;
    private List<Course> coursesList;

    private TextView txtfrgHomeNameUser;


    private EditText edtSearchHomeFrg;

    private DatabaseReference UserRef;




    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    public static String currentUserName;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);




        txtfrgHomeNameUser = view.findViewById(R.id.txtfrgHomeNameUser);
        edtSearchHomeFrg = view.findViewById(R.id.edtSearchHomeFrg);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentUserName = dataSnapshot.child("name").getValue().toString();

                txtfrgHomeNameUser.setText(currentUserName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        refSubject = FirebaseDatabase.getInstance().getReference().child("Subject");

        recyclerSubject = (RecyclerView) view.findViewById(R.id.recycleCourses);
        recyclerSubject.setHasFixedSize(true);
        layoutManagerSubject = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        subjectAdapter = new SubjectAdapter(getContext(), subjects);

        recyclerSubject.setLayoutManager(layoutManagerSubject);
        subjects = new ArrayList<>();


        subjectAdapter.setData(subjects);


        recyclerSubject.setAdapter(subjectAdapter);


        edtSearchHomeFrg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                subjectAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        recyclerAppointments = (RecyclerView) view.findViewById(R.id.recycleAppointments);
        recyclerAppointments.setHasFixedSize(true);
        layoutManagerAppointments = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        courseAdapter = new CourseAdapter(getContext(), courses);

        recyclerAppointments.setLayoutManager(layoutManagerAppointments);
        courses = new ArrayList<>();
        coursesList = new ArrayList<>();



        courseAdapter.setData(courses);

        recyclerAppointments.setAdapter(courseAdapter);


       FirebaseDatabase.getInstance().getReference()
               .child("Users").child(currentUserID)
               .child("Cours").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               coursesList.clear();
               for (DataSnapshot snapshot : dataSnapshot.getChildren())
               {
                   coursesList.add(snapshot.getValue(Course.class));
               }
               courseAdapter.setData(coursesList);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


            viewAllFiles();


        } else {
            Toast.makeText(getContext(), "No connect with internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void viewAllFiles() {

        refSubject = FirebaseDatabase.getInstance().getReference("Subject");
        refSubject.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Subject subject = postSnapShot.getValue(Subject.class);
                    subjects.add(subject);

                }

                subjectAdapter.setData(subjects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

}
