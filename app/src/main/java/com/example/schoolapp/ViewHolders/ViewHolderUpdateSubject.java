package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUpdateSubject extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtUpdateNameSubject;
    private ImageView imgRemoveSubject;


    public ViewHolderUpdateSubject(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtUpdateNameSubject = view.findViewById(R.id.txtUpdateNameSubject);

        imgRemoveSubject = view.findViewById(R.id.imgRemoveSubject);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtUpdateNameSubject() {
        return txtUpdateNameSubject;
    }


    public ImageView getImgRemoveSubject() { return imgRemoveSubject; }




}
