package com.example.schoolapp.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.R;

public class ViewHolderAppointment extends RecyclerView.ViewHolder {


    private View view;

    private TextView txtFromAppointment;
    private TextView txtToAppointment;
    private TextView txtDayAppointment;



    public ViewHolderAppointment(@NonNull View itemView) {
        super(itemView);

        this.view = itemView;


        txtFromAppointment = (TextView) view.findViewById(R.id.txtFromAppointment);
        txtToAppointment = (TextView) view.findViewById(R.id.txtToAppointment);
        txtDayAppointment = (TextView) view.findViewById(R.id.txtDayAppointment);





    }


    public View getView() {
        return view;
    }


    public TextView getTxtFromAppointment() {
        return txtFromAppointment;
    }
    public TextView getTxtToAppointment() {
        return txtToAppointment;
    }
    public TextView getTxtDayAppointment() {
        return txtDayAppointment;
    }






}
