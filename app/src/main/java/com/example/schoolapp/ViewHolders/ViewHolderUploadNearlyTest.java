package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUploadNearlyTest extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameTestUpload;
    private TextView txtDateTestUpload;
    private ImageView imgRemoveNearlyTest;

    public ViewHolderUploadNearlyTest(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;

        txtNameTestUpload = view.findViewById(R.id.txtNameTestUpload);
        txtDateTestUpload = view.findViewById(R.id.txtDateTestUpload);
        imgRemoveNearlyTest = view.findViewById(R.id.imgRemoveNearlyTest);
    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameTestUpload() {
        return txtNameTestUpload;
    }

    public TextView getTxtDateTestUpload() {
        return txtDateTestUpload;
    }

    public ImageView getImgRemoveNearlyTest() {
        return imgRemoveNearlyTest;
    }


}
