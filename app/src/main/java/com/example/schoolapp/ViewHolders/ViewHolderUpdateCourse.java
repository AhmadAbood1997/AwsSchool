package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUpdateCourse extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtUpdateNameCourse;
    private ImageView imgRemoveCourse;


    public ViewHolderUpdateCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtUpdateNameCourse = view.findViewById(R.id.txtUpdateNameCourse);

        imgRemoveCourse = view.findViewById(R.id.imgRemoveCourse);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtUpdateNameCourse() {
        return txtUpdateNameCourse;
    }


    public ImageView getImgRemoveCourse() { return imgRemoveCourse; }




}
