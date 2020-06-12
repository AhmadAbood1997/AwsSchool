package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Appointment;
import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderInsertCourse;
import com.example.schoolapp.ViewHolders.ViewHolderInsertSubject;
import com.example.schoolapp.ViewHolders.ViewHolderUpdateCourse;
import com.example.schoolapp.Views.Activities.InsertCoursesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class InsertCourseAdapter extends RecyclerView.Adapter<ViewHolderUpdateCourse> {
    private Context context;
    private List<Course> courses;
    private List<Appointment> appointments = new ArrayList<>();

    private List<LectureMp3> lectureMp3s = new ArrayList<>();
    private List<LecturePdf> lecturePdfs = new ArrayList<>();
    private List<LectureVideo> lectureVideos = new ArrayList<>();

    DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    public InsertCourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderUpdateCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_course, parent, false);
        return new ViewHolderUpdateCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUpdateCourse viewHolderUpdateCourse, int position) {


        final Course course = courses.get(position);

        viewHolderUpdateCourse.getTxtUpdateNameCourse().setText(course.getNameCourse());


        viewHolderUpdateCourse.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject").child(course.getNameSubject()).child("Cours");

                databaseReference.child(course.getNameCourse()).child("Appointment")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                appointments.clear();

                                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                                    Appointment appointment = new Appointment(postSnapShot.child("nameDay").getValue().toString(),
                                            postSnapShot.child("timeStart").getValue().toString(),
                                            postSnapShot.child("timeEnd").getValue().toString(),
                                            postSnapShot.child("nameCourseAppointment").getValue().toString());

                                    appointments.add(appointment);
                                }

                                ((InsertCoursesActivity) context).ShowCourse(appointments, course);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        viewHolderUpdateCourse.getImgRemoveCourse().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth = FirebaseAuth.getInstance();
                currentUserID = firebaseAuth.getCurrentUser().getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject")
                        .child(course.getNameSubject()).child("Cours")
                        .child(course.getNameCourse()).child("Mp3 Files");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LectureMp3 lectureMp3 = snapshot.getValue(LectureMp3.class);
                            lectureMp3s.add(lectureMp3);
                        }
                        for (LectureMp3 lectureMp3 : lectureMp3s) {
                            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lectureMp3.getLectureMp3Url());
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Subject").child(course.getNameSubject())
                                                .child("Cours").child(course.getNameCourse()).child("Mp3 Files").removeValue();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject")
                        .child(course.getNameSubject()).child("Cours")
                        .child(course.getNameCourse()).child("Video Files");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LectureVideo lectureVideo = snapshot.getValue(LectureVideo.class);
                            lectureVideos.add(lectureVideo);
                        }
                        for (LectureVideo lectureVideo : lectureVideos) {
                            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lectureVideo.getLectureVideoUrl());
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Subject").child(course.getNameSubject())
                                                .child("Cours").child(course.getNameCourse()).child("Video Files").removeValue();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject")
                        .child(course.getNameSubject()).child("Cours")
                        .child(course.getNameCourse()).child("Pdf Files");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LecturePdf lecturePdf = snapshot.getValue(LecturePdf.class);
                            lecturePdfs.add(lecturePdf);
                        }
                        for (LecturePdf lecturePdf : lecturePdfs) {
                            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lecturePdf.getLecturePdfUrl());
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Subject").child(course.getNameSubject())
                                                .child("Cours").child(course.getNameCourse()).child("Pdf Files").removeValue();

                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                lecturePdfs.clear();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject")
                        .child(course.getNameSubject()).child("Cours")
                        .child(course.getNameCourse()).child("Marks");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LecturePdf lecturePdf = snapshot.getValue(LecturePdf.class);
                            lecturePdfs.add(lecturePdf);
                        }
                        for (LecturePdf lecturePdf : lecturePdfs) {
                            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(lecturePdf.getLecturePdfUrl());
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Subject").child(course.getNameSubject())
                                                .child("Cours").child(course.getNameCourse()).removeValue();
                                        FirebaseDatabase.getInstance().getReference("Subject")
                                                .child(course.getNameSubject()).removeValue();

                                    } else {
                                        FirebaseDatabase.getInstance().getReference("Subject")
                                                .child(course.getNameSubject()).removeValue();

                                    }
                                }
                            });
                        }

                        FirebaseDatabase.getInstance().getReference("Subject")
                                .child(course.getNameSubject()).child("Cours")
                                .child(course.getNameCourse()).removeValue();


                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(currentUserID)
                                .child("Cours").child(course.getNameCourse()).removeValue();


                        FirebaseDatabase.getInstance().getReference()
                                .child("Groups").child(course.getNameCourse())
                                .removeValue();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }

        });

    }


    @Override
    public int getItemCount() {
        return courses.size();
    }


    public void setData(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

}
