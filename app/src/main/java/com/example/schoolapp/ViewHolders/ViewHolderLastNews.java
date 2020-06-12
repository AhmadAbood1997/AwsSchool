package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderLastNews extends RecyclerView.ViewHolder {


    private View view;

    private TextView txtTitleLastNews;

    private TextView txtContainLastNews;

    private TextView txtDateLastNews;


    public ViewHolderLastNews(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtTitleLastNews = (TextView) view.findViewById(R.id.txtTitleLastNews);

        txtContainLastNews = (TextView) view.findViewById(R.id.txtContainLastNews);

        txtDateLastNews = (TextView) view.findViewById(R.id.txtDateLastNews);


    }


    public View getView() {
        return view;
    }


    public TextView getTxtTitleLastNews() {
        return txtTitleLastNews;
    }

    public TextView getTxtContainLastNews() {
        return txtContainLastNews;
    }

    public TextView getTxtDateLastNews() {
        return txtDateLastNews;
    }



}
