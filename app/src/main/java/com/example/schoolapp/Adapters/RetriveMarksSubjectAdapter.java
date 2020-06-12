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
import com.example.schoolapp.ViewHolders.ViewHolderMarksSubject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetriveMarksSubjectAdapter extends RecyclerView.Adapter<ViewHolderMarksSubject> {
    private Context context;
    private List<Subject> subjects;


    private RecyclerView.LayoutManager layoutManagerCoursesMarks;
    private RetriveMarksCourseAdapter RetriveMarksCourseAdapter;
    private List<Course> courses;

    DatabaseReference databaseReference;

    public RetriveMarksSubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }



    @NonNull
    @Override
    public ViewHolderMarksSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_marks, parent, false);
        return new ViewHolderMarksSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMarksSubject viewHolderMarksSubject, final int position) {

        final Subject subject = subjects.get(position);

        viewHolderMarksSubject.getTxtNameSubjectMarks().setText(subject.getName());


        boolean isExpanded = subject.isExpanded();
        viewHolderMarksSubject.getexpandleTestCoursesMarks().setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        viewHolderMarksSubject.getRecycleTestCoursesMarks().setHasFixedSize(true);
        layoutManagerCoursesMarks = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        RetriveMarksCourseAdapter = new RetriveMarksCourseAdapter(context, courses);
        viewHolderMarksSubject.getRecycleTestCoursesMarks().setLayoutManager(layoutManagerCoursesMarks);
        courses = new ArrayList<>();

        viewAllFiles(subject,viewHolderMarksSubject);

        viewHolderMarksSubject.getBtnSubjectGoToCourseMarks().setOnClickListener(new View.OnClickListener() {
                                                                               @Override
                                                                               public void onClick(View view) {
                                                                                   subject.setExpanded(!subject.isExpanded());
                                                                                   notifyItemChanged(position);
                                                                               }
                                                                           });

        viewHolderMarksSubject.getBtnSubjectGoToCourseMarks().setBackgroundResource(isExpanded ? R.drawable.ic_down :R.drawable.ic_chevron_right);


    }


    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public void setData(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    private void viewAllFiles(Subject subject, ViewHolderMarksSubject viewHolderMarksSubject) {
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

                RetriveMarksCourseAdapter.setData(courses);
                viewHolderMarksSubject.getRecycleTestCoursesMarks().setAdapter(RetriveMarksCourseAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }



}
