package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderUploadLectureMp3;
import com.example.schoolapp.ViewHolders.ViewHolderUploadLectureVideo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UploadLectureVideoAdapter extends RecyclerView.Adapter<ViewHolderUploadLectureVideo> {
    private Context context;
    private List<LectureVideo> lectureVideos;


    DatabaseReference databaseReference;

    private AlertDialog dialog;

    public UploadLectureVideoAdapter(Context context, List<LectureVideo> lectureVideos) {
        this.context = context;
        this.lectureVideos = lectureVideos;
    }


    @NonNull
    @Override
    public ViewHolderUploadLectureVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_video_upload, parent, false);
        return new ViewHolderUploadLectureVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUploadLectureVideo viewHolderUploadLectureVideo, int position) {

        LectureVideo lectureVideo = lectureVideos.get(position);


        viewHolderUploadLectureVideo.getTxtVideoNameUpload().setText(lectureVideo.getLectureVideoName());


        viewHolderUploadLectureVideo.getImgRemoveVideo().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {


             AlertDialog.Builder builder = new AlertDialog.Builder(context);
             builder.setCancelable(false);
             builder.setView(R.layout.loading_dialog);
             dialog = builder.create();

             dialog.show();




             StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lectureVideo.getLectureVideoUrl());
             imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(UploadVideoSubjectAdapter.nameSubjectUploadVideo).child("Cours").child(UploadVideoCourseAdapter.uploadVideoNameCours).child("Video Files");
                     databaseReference.child(lectureVideo.getLectureVideoName()).removeValue()
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     if(task.isSuccessful())
                                     {
                                         dialog.dismiss();
                                     }
                                     else
                                     {
                                         Toast.makeText(context, "There is an error", Toast.LENGTH_SHORT).show();
                                         dialog.dismiss();
                                     }

                                 }
                             });
                 }
             });

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
