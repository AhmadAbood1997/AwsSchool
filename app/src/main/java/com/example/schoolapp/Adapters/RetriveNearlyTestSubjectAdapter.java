package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderNearlyTestSubject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetriveNearlyTestSubjectAdapter extends RecyclerView.Adapter<ViewHolderNearlyTestSubject> {
    private Context context;
    private List<Subject> subjects;


    DatabaseReference databaseReference;

    private RecyclerView.LayoutManager layoutManagerCoursesNearlyTests;
    private RetriveNearlyTestCourseAdapter retriveNearlyTestCourseAdapter;
    private List<Course> courses;


    public RetriveNearlyTestSubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }


    @NonNull
    @Override
    public ViewHolderNearlyTestSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_nearly_test, parent, false);
        return new ViewHolderNearlyTestSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNearlyTestSubject viewHolderNearlyTestSubject, final int position) {

        final Subject subject = subjects.get(position);

        viewHolderNearlyTestSubject.getTxtNameSubjectTest().setText(subject.getName());


        boolean isExpanded = subject.isExpanded();
        viewHolderNearlyTestSubject.getexpandleTestCourses().setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        viewHolderNearlyTestSubject.getRecycleTestCoursesNearlyTests().setHasFixedSize(true);
        layoutManagerCoursesNearlyTests = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        retriveNearlyTestCourseAdapter = new RetriveNearlyTestCourseAdapter(context, courses);
        viewHolderNearlyTestSubject.getRecycleTestCoursesNearlyTests().setLayoutManager(layoutManagerCoursesNearlyTests);
        courses = new ArrayList<>();

        viewAllFiles(subject,viewHolderNearlyTestSubject);

        viewHolderNearlyTestSubject.getBtnSubjectGoToCourse().setOnClickListener(new View.OnClickListener() {
                                                                               @Override
                                                                               public void onClick(View view) {

                                                                                   subject.setExpanded(!subject.isExpanded());
                                                                                   notifyItemChanged(position);
                                                                               }
                                                                           });

                viewHolderNearlyTestSubject.getBtnSubjectGoToCourse().setBackgroundResource(isExpanded ? R.drawable.ic_down :R.drawable.ic_chevron_right);


    }


    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public void setData(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }


    private void viewAllFiles(Subject subject, ViewHolderNearlyTestSubject viewHolderNearlyTestSubject) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName()).child("Cours");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Course course = postSnapShot.getValue(Course.class);
                    if(subject.getName().equals(course.getNameSubject()))
                        courses.add(course);
                }

                retriveNearlyTestCourseAdapter.setData(courses);
                viewHolderNearlyTestSubject.getRecycleTestCoursesNearlyTests().setAdapter(retriveNearlyTestCourseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


}
