package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderRetriveLectureVideo;
import com.example.schoolapp.Views.Activities.RetriveVideoActivity;

import java.util.List;

public class LectureVideoAdapter extends RecyclerView.Adapter<ViewHolderRetriveLectureVideo> {
    private Context context;
    private List<LectureVideo> lectureVideos;



    public LectureVideoAdapter(Context context, List<LectureVideo> lectureVideos) {
        this.context = context;
        this.lectureVideos = lectureVideos;
    }


    @NonNull
    @Override
    public ViewHolderRetriveLectureVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_video, parent, false);
        return new ViewHolderRetriveLectureVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRetriveLectureVideo viewHolderRetriveLectureVideo, int position) {

        final LectureVideo lectureVideo = lectureVideos.get(position);

        viewHolderRetriveLectureVideo.getTxtVideoName().setText(lectureVideo.getLectureVideoName());


        viewHolderRetriveLectureVideo.getexplayerView().setVideoPath(lectureVideo.getLectureVideoUrl());

        MediaController mediaController = new MediaController(context);

        viewHolderRetriveLectureVideo.getexplayerView().setMediaController(mediaController);
        mediaController.setAnchorView(viewHolderRetriveLectureVideo.getexplayerView());


        viewHolderRetriveLectureVideo.getexplayerView().start();





viewHolderRetriveLectureVideo.getimgDownload().setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ((RetriveVideoActivity) context).DownloadVideo(lectureVideo.getLectureVideoUrl(),lectureVideo.getLectureVideoName());

    }
});





    }


    @Override
    public int getItemCount() {
        return lectureVideos.size();
    }


    public void setData(List<LectureVideo> lectureVideos) {
        this.lectureVideos = lectureVideos;
        notifyDataSetChanged();
    }

}
