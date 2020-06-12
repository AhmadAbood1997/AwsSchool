package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUploadLectureVideo extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtVideoNameUpload;

    private ImageView imgRemoveVideo;



    public ViewHolderUploadLectureVideo(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtVideoNameUpload = view.findViewById(R.id.txtVideoNameUpload);

        imgRemoveVideo = view.findViewById(R.id.imgRemoveVideo);




    }


    public View getView() {
        return view;
    }


    public TextView getTxtVideoNameUpload() {
        return txtVideoNameUpload;
    }


    public ImageView getImgRemoveVideo() {
        return imgRemoveVideo;
    }





}
