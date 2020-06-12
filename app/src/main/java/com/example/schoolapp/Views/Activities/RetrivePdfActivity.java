package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.schoolapp.Adapters.RetriveLecturePdfAdapter;
import com.example.schoolapp.Adapters.RetrivePdfSubjectAdapter;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.RetrivePdfCourseAdapter.retrivePdfNameCours;

public class RetrivePdfActivity extends AppCompatActivity {


    AlertDialog dialog;
    private static final String TAG = RetrivePdfActivity.class.getName();


    DatabaseReference databaseReference;


    private RecyclerView recyclerLecturePdf;
    private RecyclerView.LayoutManager layoutManagerLecturePdf;
    private RetriveLecturePdfAdapter retriveLecturePdfAdapter;
    private List<LecturePdf> lecturePdfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_pdf);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recyclerLecturePdf = (RecyclerView) findViewById(R.id.recyclerLecturePdf);
        recyclerLecturePdf.setHasFixedSize(true);
        layoutManagerLecturePdf = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retriveLecturePdfAdapter = new RetriveLecturePdfAdapter(this, lecturePdfs);

        recyclerLecturePdf.setLayoutManager(layoutManagerLecturePdf);
        lecturePdfs = new ArrayList<>();


        retriveLecturePdfAdapter.setData(lecturePdfs);


        recyclerLecturePdf.setAdapter(retriveLecturePdfAdapter);


        if(isNetworkConnected())
        viewAllFiles();

        else if(!isNetworkConnected())
        {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }


    }

    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(RetrivePdfSubjectAdapter.nameSubjectRetrivePdf).child("Cours").child(retrivePdfNameCours).child("Pdf Files");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lecturePdfs.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    LecturePdf lecturePdf = postSnapShot.getValue(LecturePdf.class);
                    lecturePdfs.add(lecturePdf);

                }

                retriveLecturePdfAdapter.setData(lecturePdfs);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
                dialog.dismiss();

            }
        });



    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
