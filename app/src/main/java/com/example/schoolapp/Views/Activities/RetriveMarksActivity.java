package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.RetriveMarkAdapter;
import com.example.schoolapp.Adapters.RetriveMarksCourseAdapter;
import com.example.schoolapp.Adapters.RetriveMarksSubjectAdapter;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetriveMarksActivity extends AppCompatActivity {

    AlertDialog dialog;

    DatabaseReference databaseReference;

    private RecyclerView recMarkRetrive;
    private RecyclerView.LayoutManager layoutManagerMark;
    private RetriveMarkAdapter retriveMarkAdapter;
    private List<LecturePdf> lecturePdfs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_marks);
        ProfilePersonActivity.state = "Test";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();




        recMarkRetrive = (RecyclerView) findViewById(R.id.recMarkRetrive);
        recMarkRetrive.setHasFixedSize(true);
        layoutManagerMark = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retriveMarkAdapter = new RetriveMarkAdapter(this, lecturePdfs);

        recMarkRetrive.setLayoutManager(layoutManagerMark);
        lecturePdfs = new ArrayList<>();


        retriveMarkAdapter.setData(lecturePdfs);


        recMarkRetrive.setAdapter(retriveMarkAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(isNetworkConnected())
            viewAllFiles();

        else if(!isNetworkConnected())
        {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(RetriveMarksCourseAdapter.CourseCurrent.getNameSubject()).child("Cours").child(RetriveMarksCourseAdapter.markCourseName).child("Marks");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lecturePdfs.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    LecturePdf lecturePdf = postSnapShot.getValue(LecturePdf.class);
                    lecturePdfs.add(lecturePdf);

                }

                retriveMarkAdapter.setData(lecturePdfs);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();

            }
        });



    }




    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
