package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderUploadLectureMp3;
import com.example.schoolapp.Views.Activities.UploadMp3Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UploadLectureMp3Adapter extends RecyclerView.Adapter<ViewHolderUploadLectureMp3> {
    private Context context;
    private List<LectureMp3> lectureMp3s;


    DatabaseReference databaseReference;

    private AlertDialog dialog;
    
    public UploadLectureMp3Adapter(Context context, List<LectureMp3> lectureMp3s) {
        this.context = context;
        this.lectureMp3s = lectureMp3s;
    }


    @NonNull
    @Override
    public ViewHolderUploadLectureMp3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_mp3_upload, parent, false);
        return new ViewHolderUploadLectureMp3(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUploadLectureMp3 viewHolderUploadLectureMp3, int position) {

        LectureMp3 lectureMp3 = lectureMp3s.get(position);


        viewHolderUploadLectureMp3.getTxtMp3NameUpload().setText(lectureMp3.getLectureMp3Name());


     viewHolderUploadLectureMp3.getImgRemoveMp3().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {


             AlertDialog.Builder builder = new AlertDialog.Builder(context);
             builder.setCancelable(false);
             builder.setView(R.layout.loading_dialog);
             dialog = builder.create();

             dialog.show();




             StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lectureMp3.getLectureMp3Url());
             imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(UploadMp3SubjectAdapter.nameSubjectUploadMp3).child("Cours").child(UploadMp3CourseAdapter.uploadMp3NameCours).child("Mp3 Files");
                     databaseReference.child(lectureMp3.getLectureMp3Name()).removeValue()
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
        return lectureMp3s.size();
    }


    public void setData(List<LectureMp3> lectureMp3s) {
        this.lectureMp3s = lectureMp3s;
        notifyDataSetChanged();
    }





}
