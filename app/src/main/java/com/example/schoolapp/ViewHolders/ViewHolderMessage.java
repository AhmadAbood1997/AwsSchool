package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderMessage extends RecyclerView.ViewHolder {


    private View view;

    private TextView txtMessageNameCurrentUser;

    private TextView txtMessageCurrentDate;

    private TextView txtMessageCurrentTime;

    private TextView txtMessageMessageCurentUser;



    public ViewHolderMessage(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtMessageCurrentDate = (TextView) view.findViewById(R.id.txtMessageCurrentDate);
        txtMessageNameCurrentUser = (TextView) view.findViewById(R.id.txtMessageNameCurrentUser);
        txtMessageMessageCurentUser = (TextView) view.findViewById(R.id.txtMessageMessageCurentUser);
        txtMessageCurrentTime = (TextView) view.findViewById(R.id.txtMessageCurrentTime);



    }


    public View getView() {
        return view;
    }


    public TextView getTxtMessageCurrentDate() {
        return txtMessageCurrentDate;
    }

    public TextView getTxtMessageNameCurrentUser() {
        return txtMessageNameCurrentUser;
    }

    public TextView getTxtMessageMessageCurentUser() {
        return txtMessageMessageCurentUser;
    }

    public TextView getTxtMessageCurrentTime() {
        return txtMessageCurrentTime;
    }




}
