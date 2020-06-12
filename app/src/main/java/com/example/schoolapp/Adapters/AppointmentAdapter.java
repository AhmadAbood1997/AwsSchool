package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderAppointment;
import com.example.schoolapp.ViewHolders.ViewHolderCourse;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<ViewHolderAppointment> {
    private Context context;
    private List<Appointment> appointments;


    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }


    @NonNull
    @Override
    public ViewHolderAppointment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_appointment, parent, false);
        return new ViewHolderAppointment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAppointment viewHolderAppointment, int position) {

        final Appointment appointment = appointments.get(position);

        viewHolderAppointment.getTxtDayAppointment().setText(appointment.getNameDay());
        viewHolderAppointment.getTxtFromAppointment().setText(appointment.getTimeStart());
        viewHolderAppointment.getTxtToAppointment().setText(appointment.getTimeEnd());


    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public void setData(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

}
