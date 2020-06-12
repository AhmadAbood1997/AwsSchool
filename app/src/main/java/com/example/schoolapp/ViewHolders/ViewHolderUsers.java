package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderUsers extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameUser;




    public ViewHolderUsers(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameUser = (TextView) view.findViewById(R.id.txtChatName);

    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameUser() {
        return txtNameUser;
    }





}
