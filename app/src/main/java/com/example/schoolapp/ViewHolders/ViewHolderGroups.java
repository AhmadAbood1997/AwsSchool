package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderGroups extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtGroupName;




    public ViewHolderGroups(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtGroupName = (TextView) view.findViewById(R.id.txtGroupName);

    }


    public View getView() {
        return view;
    }


    public TextView geTxtGroupName() {
        return txtGroupName;
    }





}
