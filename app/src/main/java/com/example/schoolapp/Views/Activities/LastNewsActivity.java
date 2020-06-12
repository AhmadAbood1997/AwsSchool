package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schoolapp.Adapters.LastNewsAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LastNewsActivity extends AppCompatActivity {

    AlertDialog dialog;

    DatabaseReference databaseReference;

    private RecyclerView recLastNewsRetrive;
    private RecyclerView.LayoutManager layoutManagerLastNewsRetrive;
    private LastNewsAdapter lastNewsAdapter;
    private List<LastNews> lastNewsList;

    private FirebaseAuth firebaseAuth;


    private FirebaseUser firebaseUser;

    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_news);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
            currentUserID = firebaseAuth.getCurrentUser().getUid();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recLastNewsRetrive = (RecyclerView) findViewById(R.id.recLastNewsRetrive);
        recLastNewsRetrive.setHasFixedSize(true);
        layoutManagerLastNewsRetrive = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lastNewsAdapter = new LastNewsAdapter(this, lastNewsList);

        recLastNewsRetrive.setLayoutManager(layoutManagerLastNewsRetrive);
        lastNewsList = new ArrayList<>();


        lastNewsAdapter.setData(lastNewsList);


        recLastNewsRetrive.setAdapter(lastNewsAdapter);


    }


    @Override
    protected void onPause() {
        super.onPause();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("LastNews");
        databaseReference.removeValue();
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("LastNews");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastNewsList.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    LastNews lastNews = postSnapShot.getValue(LastNews.class);
                    lastNewsList.add(lastNews);

                }

                lastNewsAdapter.setData(lastNewsList);

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