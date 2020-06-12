package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.InsertNearlyTestSubjectAdapter;
import com.example.schoolapp.Adapters.RetriveNearlyTestCourseAdapter;
import com.example.schoolapp.Adapters.UploadMarkAdapter;
import com.example.schoolapp.Adapters.UploadNearlyTestAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.Models.Entities.Test;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.SendNotification.APIService;
import com.example.schoolapp.SendNotification.Client;
import com.example.schoolapp.SendNotification.Data;
import com.example.schoolapp.SendNotification.MyResponse;
import com.example.schoolapp.SendNotification.NotificationSender;
import com.example.schoolapp.Views.dialog.DialogDatePicker;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.schoolapp.Adapters.InsertNearlyTestSubjectAdapter.nameSubject;
import static com.example.schoolapp.Views.Activities.AppointmentActivity.nameCourse;
import static com.example.schoolapp.Views.Activities.InsertCoursesActivity.subject;

public class UploadNearlyTestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button btnSelectDateTest;
    private Button btnUploadTest;
    private TextView txtDateTest;
    private EditText editTextEnterNameTest;

    private AlertDialog dialog;

    private DatabaseReference refCourse;

    private APIService apiService;

    private List<User> userList;

    private DatabaseReference reference;

    private DatabaseReference databaseReference;

    private RecyclerView recUploadNearlyTest;
    private RecyclerView.LayoutManager layoutManagerUploadNearlyTest;
    private UploadNearlyTestAdapter uploadNearlyTestAdapter;
    private List<Test> tests;


    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetUploadNearlyTest;

    private FloatingActionButton fabOpenBottomSheetNearlyTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_nearly_test);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        recUploadNearlyTest = (RecyclerView) findViewById(R.id.recUploadNearlyTest);
        recUploadNearlyTest.setHasFixedSize(true);
        layoutManagerUploadNearlyTest = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        uploadNearlyTestAdapter = new UploadNearlyTestAdapter(this, tests);

        recUploadNearlyTest.setLayoutManager(layoutManagerUploadNearlyTest);
        tests = new ArrayList<>();


        uploadNearlyTestAdapter.setData(tests);


        recUploadNearlyTest.setAdapter(uploadNearlyTestAdapter);


        bottomSheetUploadNearlyTest = findViewById(R.id.bottomSheetUploadNearlyTest);


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetUploadNearlyTest);


        fabOpenBottomSheetNearlyTest = findViewById(R.id.fabOpenBottomSheetNearlyTest);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetNearlyTest.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    editTextEnterNameTest.setText("");
                    fabOpenBottomSheetNearlyTest.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        fabOpenBottomSheetNearlyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });


        reference = FirebaseDatabase.getInstance().getReference();


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        btnSelectDateTest = findViewById(R.id.btnSelectDateTest);
        btnUploadTest = findViewById(R.id.btnUploadTest);
        txtDateTest = findViewById(R.id.txtDateTest);
        editTextEnterNameTest = findViewById(R.id.editTextEnterNameTest);

        refCourse = FirebaseDatabase.getInstance().getReference().child("Subject").child(nameSubject).child("Cours")
        .child(InsertNearlyTestCourseAdapter.nameCourse).child("NearlyTest")
        ;


        userList = new ArrayList<>();

        btnSelectDateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DialogDatePicker();
                datePicker.show(getSupportFragmentManager(), "datepicker");
            }
        });

        btnUploadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextEnterNameTest.getText().toString().equals("") || txtDateTest.getText().toString().equals("")) {
                    Toast.makeText(UploadNearlyTestActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadNearlyTestActivity.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.loading_dialog);
                dialog = builder.create();

                dialog.show();


                Test test = new Test(editTextEnterNameTest.getText().toString(),
                        InsertNearlyTestCourseAdapter.nameCourse, nameSubject, txtDateTest.getText().toString());

                refCourse.child(test.getTitleForTest()).setValue(test)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("Subject")
                                    .child(nameSubject)
                                    .child("Cours")
                                    .child(InsertNearlyTestCourseAdapter.nameCourse)
                                    .child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        userList.add(snapshot.getValue(User.class));
                                    }

                                    for (User user : userList) {
                                        sendNotifications(user.getDevice_token(), "There is a nearly test", InsertNearlyTestCourseAdapter.nameCourse);

                                        reference.child("Users").child(user.getUid()).child("notification")
                                                .setValue("online");

                                        String saveCurrentTime;
                                        String saveCurrentDate;

                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                        LastNews lastNews = new LastNews("There is a nearly test", InsertNearlyTestCourseAdapter.nameCourse
                                                , saveCurrentDate + "   " + saveCurrentTime);

                                        reference.child("Users").child(user.getUid()).child("LastNews")
                                                .child(reference.push().getKey())
                                                .setValue(lastNews);

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            dialog.dismiss();
                            editTextEnterNameTest.setText("");
                            txtDateTest.setText("");
                        } else {
                            Toast.makeText(UploadNearlyTestActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            editTextEnterNameTest.setText("");
                            txtDateTest.setText("");
                        }
                    }
                });
            }
        });


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
        refCourse.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Test test = postSnapShot.getValue(Test.class);
                    tests.add(test);

                }

                uploadNearlyTestAdapter.setData(tests);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();

            }
        });


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        txtDateTest.setText(currentDateString + "");

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

}
