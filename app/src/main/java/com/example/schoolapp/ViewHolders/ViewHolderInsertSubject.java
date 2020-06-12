package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderInsertSubject extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtInsertNameSubject;
    private ImageView imgLogoSubject;


    public ViewHolderInsertSubject(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtInsertNameSubject = view.findViewById(R.id.txtInsertNameSubject);

        imgLogoSubject = view.findViewById(R.id.imgLogoSubject);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtInsertNameSubject() {
        return txtInsertNameSubject;
    }


    public ImageView getImgLogoSubject() { return imgLogoSubject; }




}
