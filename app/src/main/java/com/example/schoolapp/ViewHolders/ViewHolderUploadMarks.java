package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderUploadMarks extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameMarkUpload;

    private ImageView imgRemoveMarks;

    public ViewHolderUploadMarks(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameMarkUpload = view.findViewById(R.id.txtNameMarkUpload);

        imgRemoveMarks = view.findViewById(R.id.imgRemoveMarks);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameMarkUpload() {
        return txtNameMarkUpload;
    }

    public ImageView getImgRemoveMarks() {
        return imgRemoveMarks;
    }


}
