package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderInsertCourse;
import com.example.schoolapp.Views.Activities.RetriveMp3Activity;
import com.example.schoolapp.Views.Activities.UploadMp3Activity;

import java.util.List;

public class RetriveMp3CourseAdapter extends RecyclerView.Adapter<ViewHolderInsertCourse> {
    private Context context;
    private List<Course> courses;

    public static String retriveMp3NameCours;

    public RetriveMp3CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderInsertCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_insert_course, parent, false);
        return new ViewHolderInsertCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInsertCourse viewHolderInsertCourse, int position) {

        final Course course = courses.get(position);

        viewHolderInsertCourse.getTxtInsertNameCourse().setText(course.getNameCourse());

        viewHolderInsertCourse.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retriveMp3NameCours = course.getNameCourse();

                Intent intent = new Intent(context, RetriveMp3Activity.class);
                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return courses.size();
    }


    public void setData(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

}
