package com.example.schoolapp.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.schoolapp.R;

public class UploadLecturesActivity extends AppCompatActivity {

    private LinearLayout CardUploadMp3;
    private LinearLayout CardUploadVideo;
    private LinearLayout CardUploadPdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lectures);

        CardUploadPdf = findViewById(R.id.CardUploadPdf);
        CardUploadVideo = findViewById(R.id.CardUploadVideo);
        CardUploadMp3 = findViewById(R.id.CardUploadMp3);



        CardUploadMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadLecturesActivity.this, UploadMp3SubjectActivity.class);
                startActivity(intent);
            }
        });


        CardUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(UploadLecturesActivity.this, UploadVideoSubjectActivity.class);
                startActivity(intent);
            }
        });


        CardUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadLecturesActivity.this, UploadPdfSubjectActivity.class);
                startActivity(intent);
            }
        });




    }
}
