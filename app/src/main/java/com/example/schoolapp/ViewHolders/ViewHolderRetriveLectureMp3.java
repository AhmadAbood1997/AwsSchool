package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderRetriveLectureMp3 extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtMp3Name;

    private ImageView imgDownloadMp3;

    private ImageView playMusic;


    public ViewHolderRetriveLectureMp3(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtMp3Name = view.findViewById(R.id.txtMp3Name);

        imgDownloadMp3 = view.findViewById(R.id.imgDownloadMp3);

        playMusic = view.findViewById(R.id.playMusic);



    }


    public View getView() {
        return view;
    }


    public TextView getTxtMp3Name() {
        return txtMp3Name;
    }


    public ImageView getImgDownloadMp3() {
        return imgDownloadMp3;
    }


    public ImageView getplayMusic() {
        return playMusic;
    }



}
