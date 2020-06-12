package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertCourseAdapter;
import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.Group;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.dialog.TimePickerFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsertCoursesActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    FirebaseDatabase database;
    DatabaseReference refCourse;
    DatabaseReference reference;
    DatabaseReference refAppointment;

    DatabaseReference databaseReference;


    private RecyclerView recycleInsertCourses;
    private RecyclerView.LayoutManager layoutManagerInsertCourse;
    private InsertCourseAdapter insertCourseAdapter;
    private List<Course> courses;

    public static Subject subject;


    List<Appointment> appointments = new ArrayList<>();


    private CheckBox chkSat;

    private CheckBox chkSun;

    private CheckBox chkMon;

    private CheckBox chkTus;

    private CheckBox chkWed;

    private CheckBox chkThu;

    private CheckBox chkFri;


    private FloatingActionButton fabOpenBottomSheetCourse;

    private BottomSheetBehavior bottomSheetBehavior;

    private RelativeLayout bottomSheetInsertCourse;

    private TextView txtStartSat;
    private TextView txtEndSat;

    private TextView txtStartSun;
    private TextView txtEndSun;

    private TextView txtStartMon;
    private TextView txtEndMon;

    private TextView txtStartTus;
    private TextView txtEndTus;

    private TextView txtStartWed;
    private TextView txtEndWed;

    private TextView txtStartThu;
    private TextView txtEndThu;

    private TextView txtStartFri;
    private TextView txtEndFri;


    private boolean TimeStartSat;
    private boolean TimeEndSat;

    private boolean TimeStartSun;
    private boolean TimeEndSun;

    private boolean TimeStartMon;
    private boolean TimeEndMon;

    private boolean TimeStartTus;
    private boolean TimeEndTus;

    private boolean TimeStartWed;
    private boolean TimeEndWed;

    private boolean TimeStartThu;
    private boolean TimeEndThu;

    private boolean TimeStartFri;
    private boolean TimeEndFri;


    private Button btnAddCourse;
    private Button btnSaveCourse;

    private EditText edtBottomSheetCourseName;
    private EditText edtBottomSheetTeacherName;


    private Button btnStartSat;

    private Button btnStartSun;

    private Button btnStartMon;

    private Button btnStartTus;

    private Button btnStartWed;

    private Button btnStartThu;

    private Button btnStartFri;

    private Button btnEndSat;

    private Button btnEndSun;

    private Button btnEndMon;

    private Button btnEndTus;

    private Button btnEndWed;

    private Button btnEndThu;

    private Button btnEndFri;


    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_courses);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        defineFields();

        chkSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartSat.setText("");
                txtEndSat.setText("");
            }
        });
        chkSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartSun.setText("");
                txtEndSun.setText("");
            }
        });
        chkMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartMon.setText("");
                txtEndMon.setText("");
            }
        });
        chkTus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartTus.setText("");
                txtEndTus.setText("");
            }
        });
        chkWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartWed.setText("");
                txtEndWed.setText("");
            }
        });
        chkThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartThu.setText("");
                txtEndThu.setText("");
            }
        });
        chkFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtStartFri.setText("");
                txtEndFri.setText("");
            }
        });

        recycleInsertCourses = (RecyclerView) findViewById(R.id.recycleInsertCourses);
        recycleInsertCourses.setHasFixedSize(true);
        layoutManagerInsertCourse = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        insertCourseAdapter = new InsertCourseAdapter(this, courses);

        recycleInsertCourses.setLayoutManager(layoutManagerInsertCourse);
        courses = new ArrayList<>();


        insertCourseAdapter.setData(courses);


        recycleInsertCourses.setAdapter(insertCourseAdapter);


        bottomSheetInsertCourse = (RelativeLayout) findViewById(R.id.bottomSheetInsertCourse);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInsertCourse);


        fabOpenBottomSheetCourse = findViewById(R.id.fabOpenBottomSheetCourse);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    fabOpenBottomSheetCourse.setVisibility(View.GONE);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    clear();
                    fabOpenBottomSheetCourse.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        fabOpenBottomSheetCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                btnAddCourse.setVisibility(View.VISIBLE);

                edtBottomSheetCourseName.setVisibility(View.VISIBLE);

                btnSaveCourse.setVisibility(View.GONE);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            }
        });

        btnStartSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkSat.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartSat = true;
                }

            }
        });

        btnEndSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkSat.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndSat = true;
                }
            }
        });

        btnStartSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkSun.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartSun = true;
                }
            }
        });

        btnEndSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkSun.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndSun = true;
                }
            }
        });

        btnStartMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkMon.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartMon = true;
                }
            }
        });

        btnEndMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkMon.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndMon = true;
                }
            }
        });

        btnStartTus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkTus.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartTus = true;
                }
            }
        });

        btnEndTus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chkTus.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndTus = true;

                }
            }
        });

        btnStartWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkWed.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartWed = true;
                }
            }
        });

        btnEndWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkWed.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndWed = true;
                }
            }
        });

        btnStartThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkThu.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartThu = true;
                }
            }
        });

        btnEndThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkThu.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndThu = true;
                }
            }
        });

        btnStartFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkFri.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeStartFri = true;
                }
            }
        });

        btnEndFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkFri.isChecked()) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    TimeEndFri = true;
                }
            }
        });


        refCourse = database.getInstance().getReference().child("Subject").child(subject.getName()).child("Cours");


        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CourseName = edtBottomSheetCourseName.getText().toString().trim();
                String TeacherName = edtBottomSheetTeacherName.getText().toString().trim();


                AddAppointments(CourseName);

                if (CourseName.isEmpty()) {
                    edtBottomSheetCourseName.setError("CourseName Requierd");
                    edtBottomSheetCourseName.requestFocus();
                    return;
                }


                if (TeacherName.isEmpty()) {
                    edtBottomSheetTeacherName.setError("TeacherName Requierd");
                    edtBottomSheetTeacherName.requestFocus();
                    return;
                }


                if (isNetworkConnected()) {
                    Course course = new Course(edtBottomSheetCourseName.getText().toString(),
                            subject.getName(),
                            edtBottomSheetTeacherName.getText().toString()
                    );

                    reference = refCourse.child(course.getNameCourse());

                    reference.setValue(course);

                    refAppointment = refCourse.child(course.getNameCourse()).child("Appointment");

                    for (int i = 0; i < appointments.size(); i++) {

                        reference.child("Appointment").child(refAppointment.push().getKey()).setValue(appointments.get(i));
                    }

                    Group group = new Group(course.getNameCourse());

                    reference = FirebaseDatabase.getInstance().getReference("Groups");
                    reference.child(course.getNameCourse()).setValue(group);



                    clear();

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } else {
                    Toast.makeText(InsertCoursesActivity.this, "No connect with internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {


        if (TimeStartSat) {
            txtStartSat.setText(hour + ":" + minute);
            TimeStartSat = false;
            return;
        }

        if (TimeEndSat) {
            txtEndSat.setText(hour + ":" + minute);
            TimeEndSat = false;
            return;
        }


        if (TimeStartSun) {
            txtStartSun.setText(hour + ":" + minute);
            TimeStartSun = false;
            return;
        }

        if (TimeEndSun) {
            txtEndSun.setText(hour + ":" + minute);
            TimeEndSun = false;
            return;
        }


        if (TimeStartMon) {
            txtStartMon.setText(hour + ":" + minute);
            TimeStartMon = false;
            return;
        }

        if (TimeEndMon) {
            txtEndMon.setText(hour + ":" + minute);
            TimeEndMon = false;
            return;
        }


        if (TimeStartTus) {
            txtStartTus.setText(hour + ":" + minute);
            TimeStartTus = false;
            return;
        }

        if (TimeEndTus) {
            txtEndTus.setText(hour + ":" + minute);
            TimeEndTus = false;
            return;
        }


        if (TimeStartWed) {
            txtStartWed.setText(hour + ":" + minute);
            TimeStartWed = false;
            return;
        }

        if (TimeEndWed) {
            txtEndWed.setText(hour + ":" + minute);
            TimeEndWed = false;
            return;
        }


        if (TimeStartThu) {
            txtStartThu.setText(hour + ":" + minute);
            TimeStartThu = false;
            return;
        }

        if (TimeEndThu) {
            txtEndThu.setText(hour + ":" + minute);
            TimeEndThu = false;
            return;
        }


        if (TimeStartFri) {
            txtStartFri.setText(hour + ":" + minute);
            TimeStartFri = false;
            return;
        }

        if (TimeEndFri) {
            txtEndFri.setText(hour + ":" + minute);
            TimeEndFri = false;
            return;
        }

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void AddAppointments(String CourseName) {


        if (chkSat.isChecked()) {
            Appointment appointment = new Appointment("Sat", txtStartSat.getText().toString(), txtEndSat.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkSun.isChecked()) {
            Appointment appointment = new Appointment("Sun", txtStartSun.getText().toString(), txtEndSun.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkMon.isChecked()) {
            Appointment appointment = new Appointment("Mon", txtStartMon.getText().toString(), txtEndMon.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkTus.isChecked()) {
            Appointment appointment = new Appointment("Tus", txtStartTus.getText().toString(), txtEndTus.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkWed.isChecked()) {
            Appointment appointment = new Appointment("Wed", txtStartWed.getText().toString(), txtEndWed.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkThu.isChecked()) {
            Appointment appointment = new Appointment("Thu", txtStartThu.getText().toString(), txtEndThu.getText().toString(), CourseName);
            appointments.add(appointment);
        }

        if (chkFri.isChecked()) {
            Appointment appointment = new Appointment("Fri", txtStartFri.getText().toString(), txtEndFri.getText().toString(), CourseName);
            appointments.add(appointment);

        }
    }

    private void clear() {
        edtBottomSheetCourseName.setText("");
        edtBottomSheetTeacherName.setText("");
        txtStartSat.setText("");
        txtStartSun.setText("");
        txtStartMon.setText("");
        txtStartTus.setText("");
        txtStartWed.setText("");
        txtStartThu.setText("");
        txtStartFri.setText("");
        txtEndSat.setText("");
        txtEndSun.setText("");
        txtEndMon.setText("");
        txtEndTus.setText("");
        txtEndWed.setText("");
        txtEndThu.setText("");
        txtEndFri.setText("");

        chkSat.setChecked(false);
        chkSun.setChecked(false);
        chkMon.setChecked(false);
        chkTus.setChecked(false);
        chkWed.setChecked(false);
        chkThu.setChecked(false);
        chkFri.setChecked(false);

    }

    private void viewAllFiles() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName()).child("Cours");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    Course course = postSnapShot.getValue(Course.class);
                    if (subject.getName().equals(course.getNameSubject()))
                        courses.add(course);
                }

                insertCourseAdapter.setData(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


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
        }

    }


    private void defineFields() {


        chkSat = findViewById(R.id.chkSat);
        chkSun = findViewById(R.id.chkSun);
        chkMon = findViewById(R.id.chkMon);
        chkTus = findViewById(R.id.chkTus);
        chkWed = findViewById(R.id.chkWed);
        chkThu = findViewById(R.id.chkThu);
        chkFri = findViewById(R.id.chkFri);
        btnStartSat = findViewById(R.id.btnStartSat);
        btnStartSun = findViewById(R.id.btnStartSun);
        btnStartMon = findViewById(R.id.btnStartMon);
        btnStartTus = findViewById(R.id.btnStartTus);
        btnStartWed = findViewById(R.id.btnStartWed);
        btnStartThu = findViewById(R.id.btnStartThu);
        btnStartFri = findViewById(R.id.btnStartFri);
        btnEndSat = findViewById(R.id.btnEndSat);
        btnEndSun = findViewById(R.id.btnEndSun);
        btnEndMon = findViewById(R.id.btnEndMon);
        btnEndTus = findViewById(R.id.btnEndTus);
        btnEndWed = findViewById(R.id.btnEndWed);
        btnEndThu = findViewById(R.id.btnEndThu);
        btnEndFri = findViewById(R.id.btnEndFri);


        txtStartSat = findViewById(R.id.txtStartSat);
        txtStartSun = findViewById(R.id.txtStartSun);
        txtStartMon = findViewById(R.id.txtStartMon);
        txtStartTus = findViewById(R.id.txtStartTus);
        txtStartWed = findViewById(R.id.txtStartWed);
        txtStartThu = findViewById(R.id.txtStartThu);
        txtStartFri = findViewById(R.id.txtStartFri);
        txtEndSat = findViewById(R.id.txtEndSat);
        txtEndSun = findViewById(R.id.txtEndSun);
        txtEndMon = findViewById(R.id.txtEndMon);
        txtEndTus = findViewById(R.id.txtEndTus);
        txtEndWed = findViewById(R.id.txtEndWed);
        txtEndThu = findViewById(R.id.txtEndThu);
        txtEndFri = findViewById(R.id.txtEndFri);


        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);


        edtBottomSheetCourseName = findViewById(R.id.edtBottomSheetCourseName);
        edtBottomSheetTeacherName = findViewById(R.id.edtBottomSheetTeacherName);


    }


    public void ShowCourse(List<Appointment> appointmentList, Course courseCurrent) {
        edtBottomSheetCourseName.setVisibility(View.GONE);

        btnAddCourse.setVisibility(View.GONE);

        btnSaveCourse.setVisibility(View.VISIBLE);


        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        for (Appointment appointment : appointmentList) {
            if (appointment.getNameDay().equals("Sat")) {
                chkSat.setChecked(true);
                txtStartSat.setText(appointment.getTimeStart());
                txtEndSat.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Sun")) {
                chkSun.setChecked(true);
                txtStartSun.setText(appointment.getTimeStart());
                txtEndSun.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Mon")) {
                chkMon.setChecked(true);
                txtStartMon.setText(appointment.getTimeStart());
                txtEndMon.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Tus")) {
                chkTus.setChecked(true);
                txtStartTus.setText(appointment.getTimeStart());
                txtEndTus.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Wed")) {
                chkWed.setChecked(true);
                txtStartWed.setText(appointment.getTimeStart());
                txtEndWed.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Thu")) {
                chkThu.setChecked(true);
                txtStartThu.setText(appointment.getTimeStart());
                txtEndThu.setText(appointment.getTimeEnd());
            }
            if (appointment.getNameDay().equals("Fri")) {
                chkFri.setChecked(true);
                txtStartFri.setText(appointment.getTimeStart());
                txtEndFri.setText(appointment.getTimeEnd());
            }

        }


        edtBottomSheetTeacherName.setText(courseCurrent.getTeacherName());

        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CourseName = courseCurrent.getNameCourse();

                String TeacherName = edtBottomSheetTeacherName.getText().toString().trim();

                appointments.clear();
                AddAppointments(CourseName);


                if (TeacherName.isEmpty()) {
                    edtBottomSheetTeacherName.setError("TeacherName Requierd");
                    edtBottomSheetTeacherName.requestFocus();
                    return;
                }


                if (isNetworkConnected()) {


                    HashMap<String, Object> hashMap = new HashMap<>();


                    hashMap.put("teacherName", edtBottomSheetTeacherName.getText().toString());


                    refCourse.child(courseCurrent.getNameCourse()).updateChildren(hashMap);


                    refCourse.child(courseCurrent.getNameCourse()).child("Appointment").removeValue();


                    refAppointment = refCourse.child(courseCurrent.getNameCourse()).child("Appointment");

                    for (int i = 0; i < appointments.size(); i++) {

                        refAppointment.child(refAppointment.push().getKey()).setValue(appointments.get(i));
                    }

                    clear();

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                } else {
                    Toast.makeText(InsertCoursesActivity.this, "No connect with internet", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
