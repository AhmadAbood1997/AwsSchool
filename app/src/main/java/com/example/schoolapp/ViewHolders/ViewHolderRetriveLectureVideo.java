package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderRetriveLectureVideo extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtVideoName;
    private VideoView explayerView;
    private ImageView imgDownload;

    public ViewHolderRetriveLectureVideo(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtVideoName = view.findViewById(R.id.txtVideoName);
        explayerView = view .findViewById(R.id.explayerView);
        imgDownload = view .findViewById(R.id.imgDownload);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtVideoName() {
        return txtVideoName;
    }

    public VideoView getexplayerView() {
        return explayerView;
    }


    public ImageView getimgDownload() {
        return imgDownload;
    }






}
