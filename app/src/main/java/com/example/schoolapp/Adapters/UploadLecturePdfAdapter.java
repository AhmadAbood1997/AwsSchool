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

import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderRetriveLecturePdf;
import com.example.schoolapp.ViewHolders.ViewHolderUploadLecturePdf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.schoolapp.Adapters.UploadPdfCourseAdapter.uploadPdfNameCours;

public class UploadLecturePdfAdapter extends RecyclerView.Adapter<ViewHolderUploadLecturePdf> {
    private Context context;
    private List<LecturePdf> lecturePdfs;

    DatabaseReference databaseReference;

    private AlertDialog dialog;

    public UploadLecturePdfAdapter(Context context, List<LecturePdf> lecturePdfs) {
        this.context = context;
        this.lecturePdfs = lecturePdfs;
    }


    @NonNull
    @Override
    public ViewHolderUploadLecturePdf onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_pdf_uplode, parent, false);
        return new ViewHolderUploadLecturePdf(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUploadLecturePdf viewHolderUploadLecturePdf, int position) {

        final LecturePdf lecturePdf = lecturePdfs.get(position);

        viewHolderUploadLecturePdf.getTxtNameLecturePdfUpload().setText(lecturePdf.getLecturePdfName());

       viewHolderUploadLecturePdf.getImgRemovePdf().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setCancelable(false); // if you want user to wait for some process to finish,
               builder.setView(R.layout.loading_dialog);
               dialog = builder.create();

               dialog.show();




               StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lecturePdf.getLecturePdfUrl());
               imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(UploadPdfSubjectAdapter.nameSubjectUploadPdf).child("Cours").child(uploadPdfNameCours).child("Pdf Files");
                       databaseReference.child(lecturePdf.getLecturePdfName()).removeValue()
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
        return lecturePdfs.size();
    }


    public void setData(List<LecturePdf> lecturePdfs) {
        this.lecturePdfs = lecturePdfs;
        notifyDataSetChanged();
    }

}
