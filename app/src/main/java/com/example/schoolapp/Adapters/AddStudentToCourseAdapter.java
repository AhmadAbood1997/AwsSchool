package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Course;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderAddStudentToCourse;
import com.example.schoolapp.ViewHolders.ViewHolderInsertCourse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import static com.example.schoolapp.Views.Fragments.HomeFragment.currentUserName;

public class AddStudentToCourseAdapter extends RecyclerView.Adapter<ViewHolderAddStudentToCourse> {
    private Context context;
    private List<Course> courses;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference refCourse;


    String currentUserID;

    boolean A;

    public AddStudentToCourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }


    @NonNull
    @Override
    public ViewHolderAddStudentToCourse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_student_to_course, parent, false);
        return new ViewHolderAddStudentToCourse(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAddStudentToCourse viewHolderAddStudentToCourse, int position) {

        final Course course = courses.get(position);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUserID = firebaseAuth.getCurrentUser().getUid();


        refCourse = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID)
                .child("Cours")
                .child(course.getNameCourse());

        refCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (course.getNameCourse().equals(dataSnapshot.child("nameCourse").getValue())) {
                    A = true;
                    viewHolderAddStudentToCourse.getImgAddStudentToCourse().setImageResource(R.drawable.ic_cancel_blue);
                    return;
                } else if (!course.getNameCourse().equals(dataSnapshot.child("nameCourse").getValue())) {
                    A = false;
                    viewHolderAddStudentToCourse.getImgAddStudentToCourse().setImageResource(R.drawable.ic_add_blue);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        viewHolderAddStudentToCourse.getTxtAddStudentToCourseNameCourse().setText(course.getNameCourse());

        viewHolderAddStudentToCourse.getImgAddStudentToCourse().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (A) {

                    refCourse = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Cours").child(course.getNameCourse());
                    refCourse.removeValue();

                    refCourse = FirebaseDatabase.getInstance().getReference("Groups")
                            .child(course.getNameCourse()).child("Users")
                            .child(currentUserID);

                    refCourse.removeValue();

                    A = false;
                    return;

                }

                if (!A) {

                    viewHolderAddStudentToCourse.getImgAddStudentToCourse().setImageResource(R.drawable.ic_cancel_blue);


                    refCourse = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Cours").child(course.getNameCourse());
                    refCourse.setValue(course);

                    refCourse = FirebaseDatabase.getInstance().getReference("Groups")
                            .child(course.getNameCourse()).child("Users")
                            .child(currentUserID);

                    refCourse.setValue(currentUserID);

                    refCourse = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

                    refCourse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            FirebaseDatabase.getInstance().getReference().child("Subject")
                                    .child(course.getNameSubject())
                                    .child("Cours")
                                    .child(course.getNameCourse())
                                    .child("Users").child(currentUserID).setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    A = true;

                    return;


                }
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
