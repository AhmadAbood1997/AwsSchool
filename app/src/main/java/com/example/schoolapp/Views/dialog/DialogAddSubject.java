package com.example.schoolapp.Views.dialog;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;

public class DialogAddSubject extends DialogFragment {


    public static boolean AddSave = false;
    private String subjectName;


    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_insert_subject, container, false);
        final EditText edtDialogSubjectName = view.findViewById(R.id.edtDialogSubjectName);
        final Button btnDialogSubjectAdd = (Button) view.findViewById(R.id.btnDialogSubjectAdd);
        final Button btnDialogSubjectSave = (Button) view.findViewById(R.id.btnDialogSubjectSave);

        if (AddSave) {
            btnDialogSubjectAdd.setVisibility(View.GONE);
            btnDialogSubjectSave.setVisibility(View.VISIBLE);
            edtDialogSubjectName.setText(subjectName);

        }


        btnDialogSubjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtDialogSubjectName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_SHORT).show();
                    return;
                }


                createSubject(name);

                edtDialogSubjectName.setText("");
                dismiss();
            }
        });
        btnDialogSubjectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                subjectName = edtDialogSubjectName.getText().toString();
                if (subjectName.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                //   QuaTypCatActivity.selectedCategory.setName(categoryName);
                //    viewModelCategory.update(QuaTypCatActivity.selectedCategory);
                //   edtDialogSubjectName.setText("");

                //  updateCategory(QuaTypCatActivity.selectedCategory);

                dismiss();

            }
        });
        return view;
    }

    private void createSubject(String name) {
        Subject subject = new Subject(name);


        //     viewModelCategory.insert(category);

    }


    private void updateCategory(Subject subject) {


    }
}
