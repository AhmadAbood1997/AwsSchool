package com.example.schoolapp.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.schoolapp.R;
import com.example.schoolapp.Views.Activities.RetriveMp3Activity;
import com.example.schoolapp.Views.Activities.RetriveMp3SubjectActivity;
import com.example.schoolapp.Views.Activities.RetrivePdfActivity;
import com.example.schoolapp.Views.Activities.RetrivePdfSubjectActivity;
import com.example.schoolapp.Views.Activities.RetriveVideoActivity;
import com.example.schoolapp.Views.Activities.RetriveVideoSubjectActivity;


public class LecturesFragment extends Fragment {

    private LinearLayout CardPdf;
    private LinearLayout CardVideo;
    private LinearLayout CardMp3;

    public LecturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lectures, container, false);

        CardPdf = view.findViewById(R.id.CardPdf);
        CardVideo = view.findViewById(R.id.CardVideo);
        CardMp3 = view.findViewById(R.id.CardMp3);



        CardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RetriveVideoSubjectActivity.class);
                startActivity(intent);
            }
        });

        CardPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RetrivePdfSubjectActivity.class);
                startActivity(intent);
            }
        });

        CardMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RetriveMp3SubjectActivity.class);
                startActivity(intent);
            }
        });



        return  view;
    }
}
