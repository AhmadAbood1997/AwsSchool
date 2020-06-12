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
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.schoolapp.Adapters.RetriveLectureMp3Adapter;
import com.example.schoolapp.Adapters.RetriveMp3CourseAdapter;
import com.example.schoolapp.Adapters.RetriveMp3SubjectAdapter;
import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.schoolapp.Adapters.RetriveMp3CourseAdapter.retriveMp3NameCours;

public class RetriveMp3Activity extends AppCompatActivity {

//    private TextView txtCurrentTime, txtTotalDuration;
    private static SeekBar playerSeekBar;
 //   public static  MediaPlayer mediaPlayer = new MediaPlayer();
//    private Handler handler = new Handler();

    private boolean isExternalGranted = false;
    private final int REQUEST_EXTERNAL = 1000;

    private RecyclerView recyclerLectureMp3;
    private RecyclerView.LayoutManager layoutManagerlectureMp3;
    private RetriveLectureMp3Adapter retriveLectureMp3Adapter;
    private List<LectureMp3> lectureMp3s;

    DatabaseReference reference;

    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_mp3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recyclerLectureMp3 = (RecyclerView) findViewById(R.id.recyclerLectureMp3);
        recyclerLectureMp3.setHasFixedSize(true);
        layoutManagerlectureMp3 = new LinearLayoutManager(RetriveMp3Activity.this, LinearLayoutManager.VERTICAL, false);
        retriveLectureMp3Adapter = new RetriveLectureMp3Adapter(RetriveMp3Activity.this, lectureMp3s);

        recyclerLectureMp3.setLayoutManager(layoutManagerlectureMp3);
        lectureMp3s = new ArrayList<>();


        retriveLectureMp3Adapter.setData(lectureMp3s);


        recyclerLectureMp3.setAdapter(retriveLectureMp3Adapter);

        reference =  FirebaseDatabase.getInstance().getReference("Subject").child(RetriveMp3SubjectAdapter.nameSubjectRetriveMp3).child("Cours").child(retriveMp3NameCours).child("Mp3 Files");


        if (ContextCompat.checkSelfPermission(RetriveMp3Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(RetriveMp3Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestRuntimePermission();
        } else {
            isExternalGranted = true;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected()) {

            reference.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lectureMp3s.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        LectureMp3 lectureMp3 = postSnapShot.getValue(LectureMp3.class);
                        lectureMp3s.add(lectureMp3);

                    }

                    retriveLectureMp3Adapter.setData(lectureMp3s);

                    if (lectureMp3s != null)
                        dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(
                RetriveMp3Activity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_EXTERNAL);
    }

    public void DownloadMp3(String url, String Name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("download");
        request.setTitle("" + Name);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + Name + ".mp3");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(this, "Downloading " + Name + " Start ...", Toast.LENGTH_LONG).show();


    }


 //   Runnable upater = new Runnable() {
 //       @Override
//        public void run() {

 //           if (mediaPlayer.isPlaying()) {
 //               playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
 //               handler.postDelayed(upater, 1000);
 //           }
 //           long currentDuration = mediaPlayer.getCurrentPosition();
 //           txtCurrentTime.setText(millSecondsToTime(currentDuration));
 //       }
 //   };
//
    private String millSecondsToTime(long milliSeconds) {
        String timerString = "";
        String secondsString;


        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondsString;
        return timerString;

    }

  //  public void updateSeekBar() {
  //      if (mediaPlayer.isPlaying()) {
  //          playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
  //          handler.postDelayed(upater, 1000);
  //      }
 //   }


   /// public void upd(SeekBar playerSeekBar, MediaPlayer mediaPlayer, TextView txtCurrentTime,ImageView imgPlayPause) {
   ///     RetriveMp3Activity.this.playerSeekBar = playerSeekBar;
   ///     RetriveMp3Activity.this.mediaPlayer = mediaPlayer;
   ///     RetriveMp3Activity.this.txtCurrentTime = txtCurrentTime;



    ///    if (mediaPlayer.isPlaying()) {
    ///        handler.removeCallbacks(upater);
    ///        mediaPlayer.pause();
    ///        imgPlayPause.setImageResource(R.drawable.ic_play);
    ///    } else {
    ///        mediaPlayer.start();
    ///        imgPlayPause.setImageResource(R.drawable.ic_pause);
    ///        updateSeekBar();
    ///    }


   /// }

    @Override
    protected void onPause() {
        super.onPause();
     //   RetriveMp3Activity.this.mediaPlayer.stop();

    }
}


