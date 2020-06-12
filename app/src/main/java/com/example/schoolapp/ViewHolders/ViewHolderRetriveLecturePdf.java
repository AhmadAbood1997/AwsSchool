package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderRetriveLecturePdf extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameLecturePdf;

    public ViewHolderRetriveLecturePdf(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameLecturePdf = view.findViewById(R.id.txtNameLecturePdf);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameLecturePdf() {
        return txtNameLecturePdf;
    }




}
