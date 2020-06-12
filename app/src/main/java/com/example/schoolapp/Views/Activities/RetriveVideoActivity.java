package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.schoolapp.Adapters.LectureVideoAdapter;
import com.example.schoolapp.Adapters.RetriveVideoSubjectAdapter;
import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.RetriveVideoCourseAdapter.retriveVideoNameCours;

public class RetriveVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerLectureVideo;
    private RecyclerView.LayoutManager layoutManagerlectureVideo;
    private LectureVideoAdapter lectureVideoAdapter;
    private List<LectureVideo> lectureVideos;

    private boolean isExternalGranted = false;
    private final int REQUEST_EXTERNAL = 1000;


    DatabaseReference reference;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_video);






        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();



        recyclerLectureVideo = (RecyclerView) findViewById(R.id.recyclerLectureVideo);
        recyclerLectureVideo.setHasFixedSize(true);
        layoutManagerlectureVideo = new LinearLayoutManager(RetriveVideoActivity.this, LinearLayoutManager.VERTICAL, false);
        lectureVideoAdapter = new LectureVideoAdapter(RetriveVideoActivity.this, lectureVideos);

        recyclerLectureVideo.setLayoutManager(layoutManagerlectureVideo);
        lectureVideos = new ArrayList<>();


        lectureVideoAdapter.setData(lectureVideos);


        recyclerLectureVideo.setAdapter(lectureVideoAdapter);


        if(ContextCompat.checkSelfPermission(RetriveVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(RetriveVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            requestRuntimePermission();
        }
        else
        {
            isExternalGranted = true;
        }





    }

    @Override
    protected void onStart() {
        super.onStart();



        if(isNetworkConnected())
        {

            reference = FirebaseDatabase.getInstance().getReference("Subject").child(RetriveVideoSubjectAdapter.nameSubjectRetriveVideo)
                    .child("Cours").child(retriveVideoNameCours).child("Video Files");
            reference.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lectureVideos.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        LectureVideo lectureVideo = postSnapShot.getValue(LectureVideo.class);
                        lectureVideos.add(lectureVideo);

                    }

                    lectureVideoAdapter.setData(lectureVideos);

                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if(!isNetworkConnected())
        {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void  requestRuntimePermission()
    {
        ActivityCompat.requestPermissions(
                RetriveVideoActivity.this,
                new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_EXTERNAL);
    }

public void DownloadVideo (String url, String Name)
{
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setDescription("download");
    request.setTitle(""+Name);
// in order for this if to run, you must use the android 3.2 to compile your app
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+Name+".mp4");

// get download service and enqueue file
    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    manager.enqueue(request);

    Toast.makeText(this, "Downloading "+Name+" Start ...", Toast.LENGTH_LONG).show();


}


}
