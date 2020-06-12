package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderNearlyTestSubject extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameSubjectTest;
    private RecyclerView recycleTestCoursesNearlyTests;
    private ImageView btnSubjectGoToCourse;
    private LinearLayout expandleTestCourses;


    public ViewHolderNearlyTestSubject(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameSubjectTest = view.findViewById(R.id.txtNameSubjectTest);
        expandleTestCourses = view.findViewById(R.id.expandleTestCourses);
        recycleTestCoursesNearlyTests = view.findViewById(R.id.recycleTestCoursesNearlyTests);
        btnSubjectGoToCourse = view.findViewById(R.id.btnSubjectGoToCourse);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameSubjectTest() {
        return txtNameSubjectTest;
    }

    public LinearLayout getexpandleTestCourses() {
        return expandleTestCourses;
    }

    public ImageView getBtnSubjectGoToCourse() { return btnSubjectGoToCourse; }


    public RecyclerView getRecycleTestCoursesNearlyTests() {
        return recycleTestCoursesNearlyTests;
    }


}
