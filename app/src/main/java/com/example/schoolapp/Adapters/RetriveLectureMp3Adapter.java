package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderRetriveLectureMp3;
import com.example.schoolapp.Views.Activities.RetriveMp3Activity;

import java.util.List;

public class RetriveLectureMp3Adapter extends RecyclerView.Adapter<ViewHolderRetriveLectureMp3> {
    private Context context;
    private List<LectureMp3> lectureMp3s;


    public RetriveLectureMp3Adapter(Context context, List<LectureMp3> lectureMp3s) {
        this.context = context;
        this.lectureMp3s = lectureMp3s;
    }


    @NonNull
    @Override
    public ViewHolderRetriveLectureMp3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_mp3, parent, false);
        return new ViewHolderRetriveLectureMp3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRetriveLectureMp3 viewHolderRetriveLectureMp3, int position) {

        LectureMp3 lectureMp3 = lectureMp3s.get(position);

     //   MediaPlayer mediaPlayer = new MediaPlayer();

     //   RetriveMp3Activity.mediaPlayer = mediaPlayer;
        //prepare
      //  try {
      //      mediaPlayer.setDataSource(lectureMp3.getLectureMp3Url());
      //      mediaPlayer.prepare();
       //     viewHolderRetriveLectureMp3.getTxtTotalDuration().setText(millSecondsToTime(mediaPlayer.getDuration()));
       // } catch (Exception e) {

       // }

    //    viewHolderRetriveLectureMp3.getPlayerSeekBar().setOnTouchListener(new View.OnTouchListener() {
     //       @Override
     //       public boolean onTouch(View view, MotionEvent motionEvent) {

     //           SeekBar seekBar = (SeekBar) view;
     //           int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
     //           mediaPlayer.seekTo(playPosition);
     //           viewHolderRetriveLectureMp3.getTxtCurrentTime().setText(millSecondsToTime(mediaPlayer.getCurrentPosition()));
      //          return false;
     //       }
     //   });


       // viewHolderRetriveLectureMp3.getImgPlayPause().setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View view) {


      //          ((RetriveMp3Activity) context).upd(viewHolderRetriveLectureMp3.getPlayerSeekBar(), mediaPlayer, viewHolderRetriveLectureMp3.getTxtCurrentTime(),viewHolderRetriveLectureMp3.getImgPlayPause());


     //       }
     //   });



        viewHolderRetriveLectureMp3.getplayMusic().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( lectureMp3.getLectureMp3Url() ) );

             context.startActivity( browse );
            }
        });


        viewHolderRetriveLectureMp3.getImgDownloadMp3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RetriveMp3Activity) context).DownloadMp3(lectureMp3.getLectureMp3Url(), lectureMp3.getLectureMp3Name());

            }
        });


        viewHolderRetriveLectureMp3.getTxtMp3Name().setText(lectureMp3.getLectureMp3Name());


     //   viewHolderRetriveLectureMp3.getPlayerSeekBar().setMax(100);

    }


    @Override
    public int getItemCount() {
        return lectureMp3s.size();
    }


    public void setData(List<LectureMp3> lectureMp3s) {
        this.lectureMp3s = lectureMp3s;
        notifyDataSetChanged();
    }


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


}
