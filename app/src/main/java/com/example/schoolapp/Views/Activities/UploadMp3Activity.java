package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertMarksCourseAdapter;
import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.UploadLectureMp3Adapter;
import com.example.schoolapp.Adapters.UploadMp3CourseAdapter;
import com.example.schoolapp.Adapters.UploadMp3SubjectAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.Models.Entities.LectureMp3;
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

import static com.example.schoolapp.Adapters.RetriveMp3CourseAdapter.retriveMp3NameCours;
import static com.example.schoolapp.Adapters.UploadMp3CourseAdapter.uploadMp3NameCours;
import static com.example.schoolapp.Adapters.UploadPdfCourseAdapter.uploadPdfNameCours;

public class
UploadMp3Activity extends AppCompatActivity {

    EditText editTextEnterNameMp3;
    Button btnUploadMp3;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    ProgressBar progressBarHorizaontalMp3;

    private APIService apiService;

    private List<User> userList;

    DatabaseReference reference;

    AlertDialog dialog;


    private RecyclerView recUploadMp3;
    private RecyclerView.LayoutManager layoutManagerlectureMp3Upload;
    private UploadLectureMp3Adapter uploadLectureMp3Adapter;
    private List<LectureMp3> lectureMp3s;



    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetUploadMp3;

    private FloatingActionButton fabOpenBottomSheetMp3;


    private boolean isExternalGranted = false;
    private final int REQUEST_EXTERNAL = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_mp3);





        reference =  FirebaseDatabase.getInstance().getReference("Subject")
                .child(UploadMp3SubjectAdapter.nameSubjectUploadMp3)
                .child("Cours").child(uploadMp3NameCours).child("Mp3 Files");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recUploadMp3 = (RecyclerView) findViewById(R.id.recUploadMp3);
        recUploadMp3.setHasFixedSize(true);
        layoutManagerlectureMp3Upload = new LinearLayoutManager(UploadMp3Activity.this, LinearLayoutManager.VERTICAL, false);
        uploadLectureMp3Adapter = new UploadLectureMp3Adapter(UploadMp3Activity.this, lectureMp3s);

        recUploadMp3.setLayoutManager(layoutManagerlectureMp3Upload);
        lectureMp3s = new ArrayList<>();


        uploadLectureMp3Adapter.setData(lectureMp3s);


        recUploadMp3.setAdapter(uploadLectureMp3Adapter);

        bottomSheetUploadMp3 = findViewById(R.id.bottomSheetUploadMp3);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetUploadMp3);


        fabOpenBottomSheetMp3 = findViewById(R.id.fabOpenBottomSheetMp3);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetMp3.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    editTextEnterNameMp3.setText("");
                    fabOpenBottomSheetMp3.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        fabOpenBottomSheetMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });



        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        userList = new ArrayList<>();





        editTextEnterNameMp3 = findViewById(R.id.editTextEnterNameMp3);
        btnUploadMp3 = findViewById(R.id.btnUploadMp3);
        progressBarHorizaontalMp3 = findViewById(R.id.progressBarHorizaontalMp3);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject")
                .child(UploadMp3SubjectAdapter.nameSubjectUploadMp3).child("Cours")
                .child(uploadMp3NameCours).child("Mp3 Files");


        btnUploadMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEnterNameMp3.getText().toString().equals("")) {
                    Toast.makeText(UploadMp3Activity.this, "Please enter music's name", Toast.LENGTH_SHORT).show();
                    return;
                }


                selectMp3File();
            }
        });

        if (ContextCompat.checkSelfPermission(UploadMp3Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(UploadMp3Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestRuntimePermission();
        } else {
            isExternalGranted = true;
        }

    }

    private void selectMp3File() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Mp3 File"), 1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            uploadMp3File(data.getData());
        }

    }

    private void uploadMp3File(Uri data) {


        if (isNetworkConnected()) {
            progressBarHorizaontalMp3.setVisibility(View.VISIBLE);


            StorageReference reference = storageReference.child("uploadsMp3/" + System.currentTimeMillis() + getfileExt(data));

            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri ur1 = uri.getResult();

                            LectureMp3 lectureMp3 = new LectureMp3(editTextEnterNameMp3.getText().toString(), ur1.toString(), uploadMp3NameCours);
                            databaseReference.child(lectureMp3.getLectureMp3Name()).setValue(lectureMp3);
                            Toast.makeText(UploadMp3Activity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            progressBarHorizaontalMp3.setVisibility(View.GONE);
                            editTextEnterNameMp3.setText("");


                            FirebaseDatabase.getInstance().getReference("Subject")
                                    .child(UploadMp3SubjectAdapter.nameSubjectUploadMp3)
                                    .child("Cours")
                                    .child(uploadMp3NameCours).child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        userList.add(snapshot.getValue(User.class));

                                    }

                                    for (User user : userList) {
                                        sendNotifications(user.getDevice_token(), "There is a mp3 lecture", uploadMp3NameCours);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("notification")
                                                .setValue("online");


                                        String saveCurrentTime;
                                        String saveCurrentDate;

                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                        LastNews lastNews = new LastNews("There is a mp3 lecture", InsertNearlyTestCourseAdapter.nameCourse
                                                , saveCurrentDate +"   "+saveCurrentTime);

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
                            progressBarHorizaontalMp3.setProgress((int) progress);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadMp3Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected()) {
            reference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lectureMp3s.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        LectureMp3 lectureMp3 = postSnapShot.getValue(LectureMp3.class);
                        lectureMp3s.add(lectureMp3);

                    }

                    uploadLectureMp3Adapter.setData(lectureMp3s);

                    if (lectureMp3s != null)
                        dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private String getfileExt(Uri videoUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
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

    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(
                UploadMp3Activity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_EXTERNAL);
    }


}
