package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.MessageAdapter;
import com.example.schoolapp.Models.Entities.Contact;
import com.example.schoolapp.Models.Entities.Message;
import com.example.schoolapp.R;
import com.example.schoolapp.Views.Fragments.ContactFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    public static boolean X = false;


    private String currentGroupName;
    private String currentGroupUserID;
    private String currentGroupUserName;
    private String currentDate;
    private String currentTime;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUsers;
    private DatabaseReference databaseReferenceGroupName;
    private DatabaseReference databaseReferenceGroupMessageKey;


    private EditText edtTypeMessageGroup;


    private RecyclerView recMessages;
    private RecyclerView.LayoutManager layoutManagerMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        currentGroupUserID = firebaseAuth.getCurrentUser().getUid();

        currentGroupName = getIntent().getExtras().get("groupName").toString();

        databaseReferenceGroupName = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Messages");


        getUserInfo();


        recMessages = (RecyclerView) findViewById(R.id.recMessages);
        recMessages.setHasFixedSize(true);
        layoutManagerMessages = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, messages);

        recMessages.setLayoutManager(layoutManagerMessages);
        recMessages.setAdapter(messageAdapter);


        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custom_toolbar_group, null);
        mActionBar.setCustomView(customView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ImageButton backToMain = customView.findViewById(R.id.backToMain);
        TextView txtNameGroup = customView.findViewById(R.id.txtNameGroup);


        FloatingActionButton btnSendMessageGroup = findViewById(R.id.btnSendMessageGroup);
        edtTypeMessageGroup = findViewById(R.id.edtTypeMessageGroup);
        txtNameGroup.setText(currentGroupName);


        btnSendMessageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMessageInfoToDatabase();

                edtTypeMessageGroup.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTypeMessageGroup.getWindowToken(), 0);
            }
        });


        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //       getSupportFragmentManager().beginTransaction()
                //              .add(android.R.id.content, new MessageFragment()).commit();
                X = true;
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void saveMessageInfoToDatabase() {
        String message = edtTypeMessageGroup.getText().toString();
        String messageKey = databaseReferenceGroupName.push().getKey();


        if (TextUtils.isEmpty(message)) {
            return;
        } else {
            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calTime.getTime());


            //    HashMap<String, Object> groupMessageKey = new HashMap<>();
            //    databaseReferenceGroupName.updateChildren(groupMessageKey);

            databaseReferenceGroupMessageKey = databaseReferenceGroupName.child(currentGroupName);


            Message message1 = new Message(currentDate, message, currentGroupUserName, currentTime);
            databaseReferenceGroupName.child(databaseReferenceGroupMessageKey.push().getKey()).setValue(message1);


        }
    }

    private void getUserInfo() {
        databaseReferenceUsers.child(currentGroupUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentGroupUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseReferenceGroupName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);
                }


                messageAdapter.setData(messages);
                recMessages.smoothScrollToPosition(recMessages.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static class MessageGroupViewHolde extends RecyclerView.ViewHolder {
        TextView txtMessageNameCurrentUser;

        TextView txtMessageCurrentDate;

        TextView txtMessageCurrentTime;

        TextView txtMessageMessageCurentUser;


        public MessageGroupViewHolde(@NonNull View itemView) {
            super(itemView);
            txtMessageNameCurrentUser = itemView.findViewById(R.id.txtMessageNameCurrentUser);
            txtMessageCurrentDate = itemView.findViewById(R.id.txtMessageCurrentDate);
            txtMessageCurrentTime = itemView.findViewById(R.id.txtMessageCurrentTime);
            txtMessageMessageCurentUser = itemView.findViewById(R.id.txtMessageMessageCurentUser);

        }


    }


}


