package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderMarksSubject extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameSubjectMarks;
    private RecyclerView recycleTestCoursesMarks;
    private ImageView btnSubjectGoToCourseMarks;
    private LinearLayout expandleTestCoursesMarks;


    public ViewHolderMarksSubject(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameSubjectMarks = view.findViewById(R.id.txtNameSubjectMarks);
        expandleTestCoursesMarks = view.findViewById(R.id.expandleTestCoursesMarks);
        recycleTestCoursesMarks = view.findViewById(R.id.recycleTestCoursesMarks);
        btnSubjectGoToCourseMarks = view.findViewById(R.id.btnSubjectGoToCourseMarks);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameSubjectMarks() {
        return txtNameSubjectMarks;
    }

    public LinearLayout getexpandleTestCoursesMarks() {
        return expandleTestCoursesMarks;
    }

    public ImageView getBtnSubjectGoToCourseMarks() { return btnSubjectGoToCourseMarks; }


    public RecyclerView getRecycleTestCoursesMarks() {
        return recycleTestCoursesMarks;
    }


}
