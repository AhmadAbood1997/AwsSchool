package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderSubject extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameSubject;

    private TextView txtNumberSubject;



    private ImageView imgAdd;

    private CircleImageView img1;

    private CircleImageView img2;

    private CircleImageView img3;


    public ViewHolderSubject(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameSubject = (TextView) view.findViewById(R.id.txtNameSubject);
        txtNumberSubject = (TextView) view.findViewById(R.id.txtNumberSubject);
        img1 = (CircleImageView) view.findViewById(R.id.img1);
        img2 = (CircleImageView) view.findViewById(R.id.img2);
        img3 = (CircleImageView) view.findViewById(R.id.img3);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameCourse() {
        return txtNameSubject;
    }

    public TextView getTxtNumberCourse() {
        return txtNumberSubject;
    }




    public ImageView getImgAdd() {
        return imgAdd;
    }

    public CircleImageView getImg1() {
        return img1;
    }

    public CircleImageView getImg2() {
        return img2;
    }

    public CircleImageView getImg3() {
        return img3;
    }



}
