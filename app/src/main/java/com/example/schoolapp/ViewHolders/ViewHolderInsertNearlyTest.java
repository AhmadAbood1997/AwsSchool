package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderInsertNearlyTest extends RecyclerView.ViewHolder {


    private View view;

    private TextView txtInsertNameCourse;





    public ViewHolderInsertNearlyTest(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtInsertNameCourse = (TextView) view.findViewById(R.id.txtInsertNameCourse);





    }


    public View getView() {
        return view;
    }


    public TextView getTxtInsertNameCourse() {
        return txtInsertNameCourse;
    }





}
