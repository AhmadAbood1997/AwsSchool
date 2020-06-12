package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderCourse extends RecyclerView.ViewHolder {


    private View view;

    private TextView txtNameCourse;

    private RecyclerView recAppointments;



    public ViewHolderCourse(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameCourse = (TextView) view.findViewById(R.id.txtNameCourse);





    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameCourse() {
        return txtNameCourse;
    }

    public RecyclerView getRecAppointments() {
        return recAppointments;
    }




}
