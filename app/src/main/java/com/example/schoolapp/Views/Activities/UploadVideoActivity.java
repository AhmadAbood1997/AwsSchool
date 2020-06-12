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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.UploadLectureMp3Adapter;
import com.example.schoolapp.Adapters.UploadLectureVideoAdapter;
import com.example.schoolapp.Adapters.UploadVideoSubjectAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.SendNotification.APIService;
import com.example.schoolapp.SendNotification.Client;
import com.example.schoolapp.SendNotification.Data;
import com.example.schoolapp.SendNotification.MyResponse;
import com.example.schoolapp.SendNotification.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
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

import static com.example.schoolapp.Adapters.UploadMp3CourseAdapter.uploadMp3NameCours;
import static com.example.schoolapp.Adapters.UploadPdfCourseAdapter.uploadPdfNameCours;
import static com.example.schoolapp.Adapters.UploadVideoCourseAdapter.uploadVideoNameCours;

public class UploadVideoActivity extends AppCompatActivity {


    private static final int PICK_VIDEO_REQUEST = 1;


    private Button btnUploadVideo;
    private VideoView vidLectureVideo;
    private Uri videoUri;
    MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private EditText editTextEnterNameVideo;


    ProgressBar progressBarHorizaontalVideo;


    private APIService apiService;

    private List<User> userList;

    AlertDialog dialog;


    private RecyclerView recUploadVideo;
    private RecyclerView.LayoutManager layoutManagerlectureViedoUpload;
    private UploadLectureVideoAdapter uploadLectureVideoAdapter;
    private List<LectureVideo> lectureVideos;



    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetUploadVideo;

    private FloatingActionButton fabOpenBottomSheetVideo;


    private boolean isExternalGranted = false;
    private final int REQUEST_EXTERNAL = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();





        recUploadVideo = (RecyclerView) findViewById(R.id.recUploadVideo);
        recUploadVideo.setHasFixedSize(true);
        layoutManagerlectureViedoUpload = new LinearLayoutManager(UploadVideoActivity.this, LinearLayoutManager.VERTICAL, false);
        uploadLectureVideoAdapter = new UploadLectureVideoAdapter(UploadVideoActivity.this, lectureVideos);

        recUploadVideo.setLayoutManager(layoutManagerlectureViedoUpload);
        lectureVideos = new ArrayList<>();


        uploadLectureVideoAdapter.setData(lectureVideos);


        recUploadVideo.setAdapter(uploadLectureVideoAdapter);

        bottomSheetUploadVideo = findViewById(R.id.bottomSheetUploadVideo);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetUploadVideo);


        fabOpenBottomSheetVideo = findViewById(R.id.fabOpenBottomSheetVideo);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetVideo.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    editTextEnterNameVideo.setText("");
                    fabOpenBottomSheetVideo.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        fabOpenBottomSheetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                vidLectureVideo.setVisibility(View.VISIBLE);

            }
        });






        if (ContextCompat.checkSelfPermission(UploadVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(UploadVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestRuntimePermission();
        } else {
            isExternalGranted = true;
        }








        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        userList = new ArrayList<>();

        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        vidLectureVideo = findViewById(R.id.vidLectureVideo);
        editTextEnterNameVideo = findViewById(R.id.editTextEnterNameVideo);

        progressBarHorizaontalVideo = findViewById(R.id.progressBarHorizaontalVideo);


        mediaController = new MediaController(this);

        storageReference = FirebaseStorage.getInstance().getReference("uploadVideos");

        databaseReference = FirebaseDatabase.getInstance().getReference("Subject")
                .child(UploadVideoSubjectAdapter.nameSubjectUploadVideo).child("Cours")
                .child(uploadVideoNameCours).child("Video Files");


        vidLectureVideo.setMediaController(mediaController);
        mediaController.setAnchorView(vidLectureVideo);
        vidLectureVideo.start();


        vidLectureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vidLectureVideo.setBackground(null);
                chooseVideo();
            }
        });

        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextEnterNameVideo.getText().toString().equals("")) {
                    Toast.makeText(UploadVideoActivity.this, "Please enter video's name", Toast.LENGTH_SHORT).show();
                    return;
                }

                vidLectureVideo.setVisibility(View.GONE);
                uploadVideo();
            }
        });


    }

    private void uploadVideo() {


        if (videoUri != null && isNetworkConnected()) {

            progressBarHorizaontalVideo.setVisibility(View.VISIBLE);


            StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getfileExt(videoUri));

            reference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri ur1 = uri.getResult();


                            LectureVideo lectureVideo = new LectureVideo(editTextEnterNameVideo.getText().toString().trim(), ur1.toString(), uploadVideoNameCours);
                            databaseReference.child(lectureVideo.getLectureVideoName()).setValue(lectureVideo);
                            Toast.makeText(UploadVideoActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            progressBarHorizaontalVideo.setVisibility(View.GONE);
                            editTextEnterNameVideo.setText("");

                            FirebaseDatabase.getInstance().getReference().child("Subject")
                                    .child(UploadVideoSubjectAdapter.nameSubjectUploadVideo)
                                    .child("Cours")
                                    .child(uploadVideoNameCours).child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        userList.add(snapshot.getValue(User.class));

                                    }

                                    for(User user : userList)
                                    {
                                        sendNotifications(user.getDevice_token(),"There is a video lecture",uploadVideoNameCours);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("notification")
                                                .setValue("online");


                                        String saveCurrentTime;
                                        String saveCurrentDate;

                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                        LastNews lastNews = new LastNews("There is a video lecture", InsertNearlyTestCourseAdapter.nameCourse
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
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadVideoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBarHorizaontalVideo.setVisibility(View.GONE);



                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {


                    } else {
                        progressBarHorizaontalVideo.setVisibility(View.GONE);
                        Toast.makeText(UploadVideoActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBarHorizaontalVideo.setProgress((int) progress);
                }
            });
        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }

    private void chooseVideo() {
        Intent intent = new Intent();

        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_VIDEO_REQUEST);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null) ;

        videoUri = data.getData();

        vidLectureVideo.setVideoURI(videoUri);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkConnected()) {
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lectureVideos.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        LectureVideo lectureVideo = postSnapShot.getValue(LectureVideo.class);
                        lectureVideos.add(lectureVideo);

                    }

                    uploadLectureVideoAdapter.setData(lectureVideos);

                    if (lectureVideos != null)
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

    private String getfileExt(Uri videoUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(videoUri));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
                UploadVideoActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_EXTERNAL);
    }


}

