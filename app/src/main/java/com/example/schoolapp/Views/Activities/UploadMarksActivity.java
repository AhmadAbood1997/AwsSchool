package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertMarksCourseAdapter;
import com.example.schoolapp.Adapters.InsertMarksSubjectAdapter;
import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.InsertSubjectAdapter;
import com.example.schoolapp.Adapters.RetriveMarkAdapter;
import com.example.schoolapp.Adapters.UploadMarkAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.SendNotification.APIService;
import com.example.schoolapp.SendNotification.Client;
import com.example.schoolapp.SendNotification.Data;
import com.example.schoolapp.SendNotification.MyResponse;
import com.example.schoolapp.SendNotification.NotificationSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMarksActivity extends AppCompatActivity {

    private EditText editTextEnterNameMark;
    private ProgressBar progressBarHorizaontalMark;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    private APIService apiService;

    private List<User> userList;

    AlertDialog dialog;

    private RecyclerView recUploadMarks;
    private RecyclerView.LayoutManager layoutManagerUploadMarks;
    private UploadMarkAdapter uploadMarkAdapter;
    private List<LecturePdf> lecturePdfs;


    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetUploadMarks;

    private FloatingActionButton fabOpenBottomSheetMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_marks);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recUploadMarks = (RecyclerView) findViewById(R.id.recUploadMarks);
        recUploadMarks.setHasFixedSize(true);
        layoutManagerUploadMarks = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        uploadMarkAdapter = new UploadMarkAdapter(this, lecturePdfs);

        recUploadMarks.setLayoutManager(layoutManagerUploadMarks);
        lecturePdfs = new ArrayList<>();


        uploadMarkAdapter.setData(lecturePdfs);


        recUploadMarks.setAdapter(uploadMarkAdapter);


        bottomSheetUploadMarks = findViewById(R.id.bottomSheetUploadMarks);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetUploadMarks);


        fabOpenBottomSheetMarks = findViewById(R.id.fabOpenBottomSheetMarks);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetMarks.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    editTextEnterNameMark.setText("");
                    fabOpenBottomSheetMarks.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        fabOpenBottomSheetMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(InsertMarksSubjectAdapter.nameSubject).
                child("Cours").child(InsertMarksCourseAdapter.nameCourse).child("Marks");

        userList = new ArrayList<>();


        editTextEnterNameMark = findViewById(R.id.editTextEnterNameMark);
        Button btnUploadMark = findViewById(R.id.btnUploadMark);
        progressBarHorizaontalMark = findViewById(R.id.progressBarHorizaontalMark);

        btnUploadMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEnterNameMark.getText().toString().equals("")) {
                    Toast.makeText(UploadMarksActivity.this, "Please enter mark's name", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectPdfFile();
            }
        });


    }


    private void selectPdfFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            uploadPdfFile(data.getData());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected())
            viewAllFiles();

        else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void viewAllFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lecturePdfs.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    LecturePdf lecturePdf = postSnapShot.getValue(LecturePdf.class);
                    lecturePdfs.add(lecturePdf);

                }

                uploadMarkAdapter.setData(lecturePdfs);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();

            }
        });


    }

    private void uploadPdfFile(Uri data) {


        if (isNetworkConnected()) {
            progressBarHorizaontalMark.setVisibility(View.VISIBLE);


            StorageReference reference = storageReference.child("Marks/" + System.currentTimeMillis() + ".pdf");

            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri ur1 = uri.getResult();


                            LecturePdf lecturePdf = new LecturePdf(editTextEnterNameMark.getText().toString(), ur1.toString(), InsertMarksCourseAdapter.nameCourse);
                            databaseReference.child(lecturePdf.getLecturePdfName()).setValue(lecturePdf);
                            Toast.makeText(UploadMarksActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            progressBarHorizaontalMark.setVisibility(View.GONE);


                            FirebaseDatabase.getInstance().getReference().child("Cours")
                                    .child(InsertMarksCourseAdapter.nameCourse).child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        userList.add(snapshot.getValue(User.class));

                                    }

                                    for (User user : userList) {
                                        sendNotifications(user.getDevice_token(), "There are marks", InsertMarksCourseAdapter.nameCourse);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("notification")
                                                .setValue("online");


                                        String saveCurrentTime;
                                        String saveCurrentDate;

                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                        LastNews lastNews = new LastNews("There are marks", InsertNearlyTestCourseAdapter.nameCourse
                                                , saveCurrentDate + "   " + saveCurrentTime);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("LastNews")
                                                .child(FirebaseDatabase.getInstance().getReference().push().getKey())
                                                .setValue(lastNews);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressBarHorizaontalMark.setProgress((int) progress);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadMarksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message, "");
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                if (response.code() == 200) {
                    if (response.body().success != 1) {

                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
