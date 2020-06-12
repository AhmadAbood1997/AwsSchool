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
import com.example.schoolapp.ViewHolders.ViewHolderRetriveMarks;

import java.util.List;

public class RetriveMarkAdapter extends RecyclerView.Adapter<ViewHolderRetriveMarks> {
    private Context context;
    private List<LecturePdf> lecturePdfs;


    public RetriveMarkAdapter(Context context, List<LecturePdf> lecturePdfs) {
        this.context = context;
        this.lecturePdfs = lecturePdfs;
    }


    @NonNull
    @Override
    public ViewHolderRetriveMarks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mark_retrive, parent, false);
        return new ViewHolderRetriveMarks(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRetriveMarks viewHolderRetriveMarks, int position) {

        final LecturePdf lecturePdf = lecturePdfs.get(position);

        viewHolderRetriveMarks.getTxtNameMark().setText(lecturePdf.getLecturePdfName());

        viewHolderRetriveMarks.getView().setOnClickListener(new View.OnClickListener() {
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
