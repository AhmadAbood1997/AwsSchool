package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUploadLecturePdf extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameLecturePdfUpload;
    private ImageView ImgRemovePdf;

    public ViewHolderUploadLecturePdf(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameLecturePdfUpload = view.findViewById(R.id.txtNameLecturePdfUpload);

        ImgRemovePdf = view.findViewById(R.id.ImgRemovePdf);

    }


    public View getView() {
        return view;
    }



    public TextView getTxtNameLecturePdfUpload() {
        return txtNameLecturePdfUpload;
    }

    public ImageView getImgRemovePdf() {
        return ImgRemovePdf;
    }





}
