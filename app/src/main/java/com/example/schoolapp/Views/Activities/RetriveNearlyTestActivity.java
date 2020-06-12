package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.RetriveNearlyTestAdapter;
import com.example.schoolapp.Adapters.RetriveNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.RetriveNearlyTestSubjectAdapter;
import com.example.schoolapp.Models.Entities.Test;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetriveNearlyTestActivity extends AppCompatActivity {

    AlertDialog dialog;

    DatabaseReference databaseReference;

    private RecyclerView recNearlyTestRetrive;
    private RecyclerView.LayoutManager layoutManagerNearlyTest;
    private RetriveNearlyTestAdapter retriveNearlyTestAdapter;
    private List<Test> tests;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_nearly_test);
        ProfilePersonActivity.state = "Test";


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();



        recNearlyTestRetrive = (RecyclerView) findViewById(R.id.recNearlyTestRetrive);
        recNearlyTestRetrive.setHasFixedSize(true);
        layoutManagerNearlyTest = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retriveNearlyTestAdapter = new RetriveNearlyTestAdapter(this, tests);

        recNearlyTestRetrive.setLayoutManager(layoutManagerNearlyTest);
        tests = new ArrayList<>();


        retriveNearlyTestAdapter.setData(tests);


        recNearlyTestRetrive.setAdapter(retriveNearlyTestAdapter);


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
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(RetriveNearlyTestCourseAdapter.courseCurrent.getNameSubject()).child("Cours").child(RetriveNearlyTestCourseAdapter.nearlyTestCourseName).child("NearlyTest");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Test test = postSnapShot.getValue(Test.class);
                    tests.add(test);

                }

                retriveNearlyTestAdapter.setData(tests);

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
