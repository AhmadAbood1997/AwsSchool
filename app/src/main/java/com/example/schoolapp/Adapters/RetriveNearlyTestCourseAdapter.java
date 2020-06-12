package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderNearlyTestCourse;
import com.example.schoolapp.Views.Activities.RetriveNearlyTestActivity;

import java.util.List;

public class RetriveNearlyTestCourseAdapter extends RecyclerView.Adapter<ViewHolderNearlyTestCourse> {
    private Context context;
    private List<Course> Courses;
    public static String nearlyTestCourseName;
    public static Course courseCurrent;

    public RetriveNearlyTestCourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.Courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderNearlyTestCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_nearly_test, parent, false);
        return new ViewHolderNearlyTestCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNearlyTestCourse viewHolderNearlyTestCourse, int position) {

        final Course course = Courses.get(position);

        viewHolderNearlyTestCourse.getTxtNameCourseTest().setText(course.getNameCourse());

        viewHolderNearlyTestCourse.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearlyTestCourseName = course.getNameCourse();
                courseCurrent = course;
                Intent intent = new Intent(context, RetriveNearlyTestActivity.class);
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
