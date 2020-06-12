package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderRetriveNearlyTest extends RecyclerView.ViewHolder {
    private View view;

    private TextView txtNameTest;
    private TextView txtDateTest;

    public ViewHolderRetriveNearlyTest(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtNameTest = view.findViewById(R.id.txtNameTest);
        txtDateTest = view.findViewById(R.id.txtDateTest);
    }


    public View getView() {
        return view;
    }


    public TextView getTxtNameTest() {
        return txtNameTest;
    }

    public TextView getTxtDateTest() {
        return txtDateTest;
    }



}
