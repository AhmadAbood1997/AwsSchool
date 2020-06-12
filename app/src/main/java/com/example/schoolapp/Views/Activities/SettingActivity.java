package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.schoolapp.Models.Entities.Upload;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView imgSettingPerson;
    private EditText editTextSettingUsername;
    private EditText editTextSettingStatus;
    private Button btnSettingSave;


    private String currentUserID;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    private RadioButton rdArabic;
    private RadioButton rdEnglish;

    public static boolean languae;

    private static final int GalleryPick = 1;
    private StorageReference UserProfileImageRef;

    AlertDialog dialog;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        define();





        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnSettingSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSetting();
                if(rdArabic.isChecked())
                {
                    setLanguage(SettingActivity.this,"ar");

                }
                else if(rdEnglish.isChecked())
                {
                    setLanguage(SettingActivity.this,"en");

                }
            }
        });

        RetriveUserInfo();


        imgSettingPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GalleryPick);

            }
        });

    }

    private void RetriveUserInfo() {
        databaseReference.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image"))) {
                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();
                            String retriveprofileImage = dataSnapshot.child("image").getValue().toString();


                            editTextSettingUsername.setText(retriveUserName);
                            editTextSettingStatus.setText(retriveStatus);
                            if (!retriveprofileImage.equals(""))
                                Picasso.get().load(retriveprofileImage).into(imgSettingPerson);


                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                            String retriveUserName = dataSnapshot.child("name").getValue().toString();
                            String retriveStatus = dataSnapshot.child("status").getValue().toString();

                            editTextSettingUsername.setText(retriveUserName);
                            editTextSettingStatus.setText(retriveStatus);
                        } else {
                            Toast.makeText(SettingActivity.this, "Please set your profile information...", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void UpdateSetting() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        String setUserName = editTextSettingUsername.getText().toString();
        String setStatus = editTextSettingStatus.getText().toString();

        if (setUserName.isEmpty()) {
            editTextSettingUsername.setError("UserName Requierd");
            editTextSettingUsername.requestFocus();
            dialog.dismiss();
            return;
        }

        if (setStatus.isEmpty()) {
            editTextSettingStatus.setError("Status Requierd");
            editTextSettingStatus.requestFocus();
            dialog.dismiss();
            return;
        }
        String deviceToken = FirebaseInstanceId.getInstance().getToken();


        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("uid", currentUserID);
        profileMap.put("name", setUserName);
        profileMap.put("status", setStatus);
        profileMap.put("device_token", deviceToken);
        profileMap.put("notification", "offline");




        databaseReference.child("Users").child(currentUserID)
                .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    toMainActivity();
                } else {
                    dialog.dismiss();
                    Toast.makeText(SettingActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        })

        ;


        StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
        if (resultUri != null) {
            filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {


                        Uri downloadUri = task.getResult();

                        HashMap<String, String> profileMap = new HashMap<>();

                        if (downloadUri == null) {
                            Toast.makeText(SettingActivity.this, "ee", Toast.LENGTH_SHORT).show();
                        }

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        profileMap.put("image", downloadUri.toString());
                        profileMap.put("uid", currentUserID);
                        profileMap.put("device_token", deviceToken);
                        profileMap.put("name", editTextSettingUsername.getText().toString());
                        profileMap.put("status", editTextSettingStatus.getText().toString());
                        profileMap.put("notification", "offline");

                        databaseReference.child("Users").child(currentUserID)
                                .setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    dialog.dismiss();
                                    toMainActivity();

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    } else {
                        Toast.makeText(SettingActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.get().load(resultUri).into(imgSettingPerson);

            } else {
            }
        }
    }

    private void define() {
        imgSettingPerson = findViewById(R.id.imgSettingPerson);
        editTextSettingUsername = findViewById(R.id.editTextSettingUsername);
        editTextSettingStatus = findViewById(R.id.editTextSettingStatus);
        btnSettingSave = findViewById(R.id.btnSettingSave);
        rdArabic = findViewById(R.id.rdArabic);
        rdEnglish = findViewById(R.id.rdEnglish);
    }

    @SuppressWarnings("deprecation")
    public void setLocale(String lang)
    {
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Configuration conf = getResources().getConfiguration();
        conf.locale = myLocale;
        getResources().updateConfiguration(conf,dm);
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }


    public void setLanguage(Context c, String lang) {
        Locale localeNew = new Locale(lang);
        Locale.setDefault(localeNew);

        Resources res = c.getResources();
        Configuration newConfig = new Configuration(res.getConfiguration());
        newConfig.locale = localeNew;
        newConfig.setLayoutDirection(localeNew);
        res.updateConfiguration(newConfig, res.getDisplayMetrics());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newConfig.setLocale(localeNew);
            c.createConfigurationContext(newConfig);
        }
    }



    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
