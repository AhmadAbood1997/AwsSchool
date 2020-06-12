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
import com.example.schoolapp.ViewHolders.ViewHolderMarksCourse;
import com.example.schoolapp.Views.Activities.RetriveMarksActivity;

import java.util.List;

public class RetriveMarksCourseAdapter extends RecyclerView.Adapter<ViewHolderMarksCourse> {
    private Context context;
    private List<Course> Courses;

    public static String markCourseName;

    public static Course CourseCurrent;


    public RetriveMarksCourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.Courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderMarksCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_marks, parent, false);
        return new ViewHolderMarksCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMarksCourse ViewHolderMarksCourse, int position) {

        final Course course = Courses.get(position);


        ViewHolderMarksCourse.getTxtNameCourseMarks().setText(course.getNameCourse());

        ViewHolderMarksCourse.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markCourseName = course.getNameCourse();
                CourseCurrent = course;
                Intent intent = new Intent(context, RetriveMarksActivity.class);
                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return Courses.size();
    }


    public void setData(List<Course> Courses) {
        this.Courses = Courses;
        notifyDataSetChanged();
    }

}
