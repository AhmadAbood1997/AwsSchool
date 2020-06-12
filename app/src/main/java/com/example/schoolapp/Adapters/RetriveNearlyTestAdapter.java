package com.example.schoolapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolapp.Models.Entities.Test;
import com.example.schoolapp.R;
import com.example.schoolapp.ViewHolders.ViewHolderRetriveNearlyTest;

import java.util.List;

public class RetriveNearlyTestAdapter extends RecyclerView.Adapter<ViewHolderRetriveNearlyTest> {
    private Context context;
    private List<Test> tests;


    public RetriveNearlyTestAdapter(Context context, List<Test> tests) {
        this.context = context;
        this.tests = tests;
    }


    @NonNull
    @Override
    public ViewHolderRetriveNearlyTest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nearly_test_retrive, parent, false);
        return new ViewHolderRetriveNearlyTest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRetriveNearlyTest viewHolderRetriveNearlyTest, int position) {

        Test test = tests.get(position);

        viewHolderRetriveNearlyTest.getTxtNameTest().setText(test.getTitleForTest());

        viewHolderRetriveNearlyTest.getTxtDateTest().setText(test.getTestDate());


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
