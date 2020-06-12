package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderCourse;
import com.example.schoolapp.Views.Activities.AppointmentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<ViewHolderCourse> {
    private Context context;
    private List<Course> courses;
    public static Course courseCurrent;


    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolderCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCourse viewHolderCourse, int position) {

        final Course course = courses.get(position);





        viewHolderCourse.getTxtNameCourse().setText(course.getNameCourse());


        viewHolderCourse.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentActivity.nameCourse = course.getNameCourse();
                courseCurrent = course;
                Intent intent = new Intent(context,AppointmentActivity.class);
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
