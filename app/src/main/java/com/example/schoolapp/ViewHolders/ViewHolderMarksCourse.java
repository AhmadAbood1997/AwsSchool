package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderMarksCourse extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameCourseMarks;

    public ViewHolderMarksCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameCourseMarks = view.findViewById(R.id.txtNameCourseMarks);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameCourseMarks() {
        return txtNameCourseMarks;
    }




}
