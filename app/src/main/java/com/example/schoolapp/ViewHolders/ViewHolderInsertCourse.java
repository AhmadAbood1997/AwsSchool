package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderInsertCourse extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtInsertNameCourse;
    private ImageView imgLogoCourse;


    public ViewHolderInsertCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtInsertNameCourse = view.findViewById(R.id.txtInsertNameCourse);

        imgLogoCourse = view.findViewById(R.id.imgLogoCourse);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtInsertNameCourse() {
        return txtInsertNameCourse;
    }


    public ImageView getImgLogoCourse() { return imgLogoCourse; }




}
