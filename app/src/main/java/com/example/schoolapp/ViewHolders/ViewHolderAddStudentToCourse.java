package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderAddStudentToCourse extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtAddStudentToCourseNameCourse;

    private ImageView imgAddStudentToCourse;



    public ViewHolderAddStudentToCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtAddStudentToCourseNameCourse = view.findViewById(R.id.txtAddStudentToCourseNameCourse);

        imgAddStudentToCourse = view.findViewById(R.id.imgAddStudentToCourse);




    }


    public View getView() {
        return view;
    }


    public TextView getTxtAddStudentToCourseNameCourse() {
        return txtAddStudentToCourseNameCourse;
    }


    public ImageView getImgAddStudentToCourse() {
        return imgAddStudentToCourse;
    }





}
