package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderRetriveMarks extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameMark;

    public ViewHolderRetriveMarks(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameMark = view.findViewById(R.id.txtNameMark);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameMark() {
        return txtNameMark;
    }




}
