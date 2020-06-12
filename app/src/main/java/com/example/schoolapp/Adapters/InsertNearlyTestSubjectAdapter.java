package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderInsertSubject;
import com.example.schoolapp.Views.Activities.InsertCoursesActivity;
import com.example.schoolapp.Views.Activities.InsertNearlyTestCourseActivity;
import com.example.schoolapp.Views.Activities.InsertSubjectsActivity;

import java.util.List;

public class InsertNearlyTestSubjectAdapter extends RecyclerView.Adapter<ViewHolderInsertSubject> {
    private Context context;
    private List<Subject> subjects;
    public static String nameSubject;

    public InsertNearlyTestSubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }


    @NonNull
    @Override
    public ViewHolderInsertSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_insert_subject, parent, false);
        return new ViewHolderInsertSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInsertSubject viewHolderInsertSubject, int position) {

        final Subject subject = subjects.get(position);


        viewHolderInsertSubject.getTxtInsertNameSubject().setText(subject.getName());


        viewHolderInsertSubject.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameSubject = subject.getName();

                Intent intent = new Intent(context, InsertNearlyTestCourseActivity.class);

                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public void setData(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }


}
