package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.ViewHolders.ViewHolderSubject;
import com.example.schoolapp.Views.Activities.AddStudentToCourseActivity;
import com.example.schoolapp.Views.Activities.InsertCoursesActivity;
import com.example.schoolapp.Views.Fragments.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<ViewHolderSubject> implements Filterable {
    private Context context;
    private List<Subject> subjects;
    private List<Subject> subjectsFilter;


    private List<Course> courses = new ArrayList<>();

    DatabaseReference refSubject;

    public static String nameSubject;

    public SubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
        this.subjectsFilter = subjects;
    }

    public SubjectAdapter(List<Subject> subjects) {
        this.subjects = subjects;
        this.subjectsFilter = subjects;
    }

    @NonNull
    @Override
    public ViewHolderSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolderSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSubject viewHolderSubject, int position) {

        final Subject subject = subjectsFilter.get(position);

        viewHolderSubject.getTxtNameCourse().setText(subjectsFilter.get(position).getName());


        refSubject = FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName()).child("Cours");
        refSubject.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Course course = postSnapShot.getValue(Course.class);
                    if (subject.getName().equals(course.getNameSubject()))
                        courses.add(course);


                }
                viewHolderSubject.getTxtNumberCourse().setText(courses.size() + " " + "Courses");

                viewHolderSubject.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        nameSubject = subject.getName();
                        Intent intent = new Intent(context, AddStudentToCourseActivity.class);
                        InsertCoursesActivity.subject = subject;
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return subjectsFilter.size();
    }


    public void setData(List<Subject> subjects) {
        this.subjectsFilter = subjects;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if (Key.isEmpty()) {
                    subjectsFilter = HomeFragment.subjects;
                } else {
                    List<Subject> lstFilterd = new ArrayList<>();
                    for (Subject subject : subjectsFilter) {
                        if (subject.getName().toLowerCase().contains(Key.toLowerCase())) {
                            lstFilterd.add(subject);
                        }
                    }

                    subjectsFilter = lstFilterd;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = subjectsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subjectsFilter = (List<Subject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
