package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.schoolapp.Views.Activities.GroupChatActivity.X;

import com.example.schoolapp.Adapters.FindFriendAdapter;
import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsActivity extends AppCompatActivity {


    private RecyclerView recycleFindFriends;
    private RecyclerView.LayoutManager layoutManagerFindFriends;
    private FindFriendAdapter findFriendAdapter;
    private List<User> users;


    DatabaseReference reference;

    AlertDialog dialog;

    private ImageButton btnToMainFromFind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        btnToMainFromFind = findViewById(R.id.btnToMainFromFind);

        btnToMainFromFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X = true;
                Intent intent = new Intent(FindFriendsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        recycleFindFriends = (RecyclerView) findViewById(R.id.recycleFindFriends);
        recycleFindFriends.setHasFixedSize(true);
        layoutManagerFindFriends = new LinearLayoutManager(FindFriendsActivity.this, LinearLayoutManager.VERTICAL, false);
        findFriendAdapter = new FindFriendAdapter(FindFriendsActivity.this, users);

        recycleFindFriends.setLayoutManager(layoutManagerFindFriends);
        users = new ArrayList<>();


        findFriendAdapter.setData(users);


        recycleFindFriends.setAdapter(findFriendAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        if (isNetworkConnected()) {

            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        User user = postSnapShot.getValue(User.class);
                        users.add(user);

                    }

                    findFriendAdapter.setData(users);

                    if (users != null)
                        dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (!isNetworkConnected()) {
            Toast.makeText(this, "No connect with internet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(UploadVideoActivity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
