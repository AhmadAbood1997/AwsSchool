package com.example.schoolapp.Views.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.schoolapp.Models.Entities.Group;
import com.example.schoolapp.Models.Entities.Subject;
import com.example.schoolapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogAddGroup extends DialogFragment {

    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_group, container, false);
        final EditText edtDialogGroupName = view.findViewById(R.id.edtDialogGroupName);
        final Button btnDialogGroupAdd = view.findViewById(R.id.btnDialogGroupAdd);

        reference = FirebaseDatabase.getInstance().getReference();


        btnDialogGroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = edtDialogGroupName.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    Toast.makeText(getContext(), "Please write group name...", Toast.LENGTH_SHORT).show();
                } else {
                    Group group = new Group(groupName);
                    createNewGroup(group);
                    dismiss();
                }
            }
        });

        return view;
    }

    private void createNewGroup(Group group) {

        reference.child("Groups").child(group.getNameGroup()).child("nameGroup").setValue(group.getNameGroup());
       // reference.child("Groups").child(group.getGroupName()).child("user1").setValue(group);


        //    .addOnCompleteListener(new OnCompleteListener<Void>() {
        //        @Override
        //        public void onComplete(@NonNull Task<Void> task) {
        //         if(task.isSuccessful())
        //
        //       }
        //   });
    }


}
