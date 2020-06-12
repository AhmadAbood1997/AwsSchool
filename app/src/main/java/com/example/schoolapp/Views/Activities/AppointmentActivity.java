package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.AppointmentAdapter;
import com.example.schoolapp.Adapters.CourseAdapter;
import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {


    DatabaseReference databaseReference;


    private RecyclerView recAppointment;
    private RecyclerView.LayoutManager layoutManagerInsertSubject;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointments;

     public static String nameCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);


        recAppointment = (RecyclerView) findViewById(R.id.recAppointment);
        recAppointment.setHasFixedSize(true);
        layoutManagerInsertSubject = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appointmentAdapter = new AppointmentAdapter(this, appointments);

        recAppointment.setLayoutManager(layoutManagerInsertSubject);
        appointments = new ArrayList<>();


        appointmentAdapter.setData(appointments);


        recAppointment.setAdapter(appointmentAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject").child(CourseAdapter.courseCurrent.getNameSubject()).child("Cours");


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected())
            viewAllFiles();

        else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewAllFiles() {

        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

             databaseReference.child(postSnapShot.getKey()).child("nameCourse").addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     if(dataSnapshot.getValue().toString().equals(nameCourse))
                     {

                         databaseReference.child(postSnapShot.getKey()).child("Appointment")
                                 .addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         appointments.clear();

                                         for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                                             Appointment appointment = new Appointment(postSnapShot.child("nameDay").getValue().toString(),
                                                     postSnapShot.child("timeStart").getValue().toString(),
                                                     postSnapShot.child("timeEnd").getValue().toString(),
                                                     postSnapShot.child("nameCourseAppointment").getValue().toString());

                                             appointments.add(appointment);
                                         }


                                         appointmentAdapter.setData(appointments);

                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                     }
                                 });



                     }

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });




                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
