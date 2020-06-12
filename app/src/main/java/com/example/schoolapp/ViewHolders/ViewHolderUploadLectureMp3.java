package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUploadLectureMp3 extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtMp3NameUpload;

    private ImageView imgRemoveMp3;



    public ViewHolderUploadLectureMp3(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtMp3NameUpload = view.findViewById(R.id.txtMp3NameUpload);

        imgRemoveMp3 = view.findViewById(R.id.imgRemoveMp3);




    }


    public View getView() {
        return view;
    }


    public TextView getTxtMp3NameUpload() {
        return txtMp3NameUpload;
    }


    public ImageView getImgRemoveMp3() {
        return imgRemoveMp3;
    }





}
