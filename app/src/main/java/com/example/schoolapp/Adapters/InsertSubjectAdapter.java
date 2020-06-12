package com.example.schoolapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.LectureMp3;
import com.example.schoolapp.Models.Entities.LecturePdf;
import com.example.schoolapp.Models.Entities.LectureVideo;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderInsertSubject;
import com.example.schoolapp.ViewHolders.ViewHolderSubject;
import com.example.schoolapp.ViewHolders.ViewHolderUpdateSubject;
import com.example.schoolapp.Views.Activities.InsertCoursesActivity;
import com.example.schoolapp.Views.Activities.InsertSubjectsActivity;
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

public class InsertSubjectAdapter extends RecyclerView.Adapter<ViewHolderUpdateSubject> {
    private Context context;
    private List<Subject> subjects;


    private List<Course> coursesList = new ArrayList<>();
    private List<LectureMp3> lectureMp3s = new ArrayList<>();
    private List<LecturePdf> lecturePdfs = new ArrayList<>();
    private List<LectureVideo> lectureVideos = new ArrayList<>();

    private DatabaseReference refCourse;

    private FirebaseAuth firebaseAuth;
    private String currentUserID;


    public InsertSubjectAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public ViewHolderUpdateSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_subject, parent, false);
        return new ViewHolderUpdateSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUpdateSubject viewHolderUpdateSubject, int position) {

        final Subject subject = subjects.get(position);

        viewHolderUpdateSubject.getTxtUpdateNameSubject().setText(subject.getName());


        viewHolderUpdateSubject.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertCoursesActivity.subject = subject;

                Intent intent = new Intent(context, InsertCoursesActivity.class);

                context.startActivity(intent);

            }
        });

        viewHolderUpdateSubject.getImgRemoveSubject().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                firebaseAuth = FirebaseAuth.getInstance();

                currentUserID = firebaseAuth.getCurrentUser().getUid();


                FirebaseDatabase.getInstance().getReference().child("Subject")
                        .child(subject.getName()).child("Cours")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                coursesList.clear();


                                for (DataSnapshot postSnapShotCourse : dataSnapshot.getChildren()) {
                                    coursesList.add(postSnapShotCourse.getValue(Course.class));
                                }


                                for (Course course : coursesList) {

                                    refCourse = FirebaseDatabase.getInstance().getReference().child("Subject")
                                            .child(subject.getName()).child("Cours")
                                            .child(course.getNameCourse()).child("Mp3 Files");

                                    refCourse.addValueEventListener(new ValueEventListener() {
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
                                                            FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName())
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

                                    refCourse = FirebaseDatabase.getInstance().getReference().child("Subject")
                                            .child(subject.getName()).child("Cours")
                                            .child(course.getNameCourse()).child("Video Files");
                                    refCourse.addValueEventListener(new ValueEventListener() {
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
                                                            FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName())
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

                                    refCourse = FirebaseDatabase.getInstance().getReference().child("Subject")
                                            .child(subject.getName()).child("Cours")
                                            .child(course.getNameCourse()).child("Pdf Files");
                                    refCourse.addValueEventListener(new ValueEventListener() {
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
                                                            FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName())
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

                                    refCourse = FirebaseDatabase.getInstance().getReference().child("Subject")
                                            .child(subject.getName()).child("Cours")
                                            .child(course.getNameCourse()).child("Marks");
                                    refCourse.addValueEventListener(new ValueEventListener() {
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
                                                            FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName())
                                                                    .child("Cours").child(course.getNameCourse()).removeValue();
                                                            FirebaseDatabase.getInstance().getReference("Subject")
                                                                    .child(subject.getName()).removeValue();

                                                        } else {
                                                            FirebaseDatabase.getInstance().getReference("Subject")
                                                                    .child(subject.getName()).removeValue();

                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });


                                }

                                FirebaseDatabase.getInstance().getReference().child("Subject")
                                        .child(subject.getName()).child("Cours")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                coursesList.clear();


                                                for (DataSnapshot postSnapShotCourse : dataSnapshot.getChildren()) {
                                                    coursesList.add(postSnapShotCourse.getValue(Course.class));
                                                }



                                                for(Course course : coursesList)
                                                {

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users").child(currentUserID)
                                                            .child("Cours").child(course.getNameCourse()).removeValue();


                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Groups").child(course.getNameCourse())
                                                            .removeValue();


                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                FirebaseDatabase.getInstance().getReference("Subject").child(subject.getName()).removeValue();


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
        return subjects.size();
    }


    public void setData(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }


}
