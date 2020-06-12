package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderNearlyTestCourse extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameCourseTest;

    public ViewHolderNearlyTestCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameCourseTest = view.findViewById(R.id.txtNameCourseTest);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameCourseTest() {
        return txtNameCourseTest;
    }




}
