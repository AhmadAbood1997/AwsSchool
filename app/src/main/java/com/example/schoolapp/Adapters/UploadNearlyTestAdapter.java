package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Test;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderUploadNearlyTest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UploadNearlyTestAdapter extends RecyclerView.Adapter<ViewHolderUploadNearlyTest> {
    private Context context;
    private List<Test> tests;
    DatabaseReference databaseReference;

    public UploadNearlyTestAdapter(Context context, List<Test> tests) {
        this.context = context;
        this.tests = tests;
    }


    @NonNull
    @Override
    public ViewHolderUploadNearlyTest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nearly_test_upload, parent, false);
        return new ViewHolderUploadNearlyTest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUploadNearlyTest viewHolderUploadNearlyTest, int position) {

        Test test = tests.get(position);

        viewHolderUploadNearlyTest.getTxtNameTestUpload().setText(test.getTitleForTest());

        viewHolderUploadNearlyTest.getTxtDateTestUpload().setText(test.getTestDate());

        viewHolderUploadNearlyTest.getImgRemoveNearlyTest().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference = FirebaseDatabase.getInstance().getReference("Subject").child(InsertNearlyTestSubjectAdapter.nameSubject).child("Cours").child(InsertNearlyTestCourseAdapter.nameCourse).child("NearlyTest");
                databaseReference.child(test.getTitleForTest()).removeValue();


            }
        });

    }


    @Override
    public int getItemCount() {
        return tests.size();
    }


    public void setData(List<Test> tests) {
        this.tests = tests;
        notifyDataSetChanged();
    }

}
