package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderFindFriend extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtFindFriendStatus;
    private TextView txtFindFriendName;
    private TextView txtFindFriendOnline;
    private CircleImageView imgFindFriendProfile;




    public ViewHolderFindFriend(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtFindFriendStatus =  view.findViewById(R.id.txtFindFriendStatus);
        txtFindFriendName =  view.findViewById(R.id.txtFindFriendName);
        txtFindFriendOnline =  view.findViewById(R.id.txtFindFriendOnline);
        imgFindFriendProfile =  view.findViewById(R.id.imgFindFriendProfile);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtFindFriendStatus() {
        return txtFindFriendStatus;
    }
    public TextView getTxtFindFriendName() {
        return txtFindFriendName;
    }
    public TextView getTxtFindFriendOnline() {
        return txtFindFriendOnline;
    }
    public CircleImageView getImgFindFriendProfile() {
        return imgFindFriendProfile;
    }







}
