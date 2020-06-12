package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderRetriveLecturePdf;

import java.util.List;

public class RetriveLecturePdfAdapter extends RecyclerView.Adapter<ViewHolderRetriveLecturePdf> {
    private Context context;
    private List<LecturePdf> lecturePdfs;


    public RetriveLecturePdfAdapter(Context context, List<LecturePdf> lecturePdfs) {
        this.context = context;
        this.lecturePdfs = lecturePdfs;
    }


    @NonNull
    @Override
    public ViewHolderRetriveLecturePdf onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lecture_pdf, parent, false);
        return new ViewHolderRetriveLecturePdf(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRetriveLecturePdf viewHolderRetriveLecturePdf, int position) {

        final LecturePdf lecturePdf = lecturePdfs.get(position);

        viewHolderRetriveLecturePdf.getTxtNameLecturePdf().setText(lecturePdf.getLecturePdfName());

        viewHolderRetriveLecturePdf.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( lecturePdf.getLecturePdfUrl() ) );
                context.startActivity( browse );

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
