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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.MessageAdapter;
import com.example.schoolapp.Adapters.MessageChatAdapter;
import com.example.schoolapp.Models.Entities.MessageChat;
import com.example.schoolapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.schoolapp.R.layout.activity_chat;

public class ChatActivity extends AppCompatActivity {


    private String messageSenderID;
    private String messageReceiverID;
    private String receiveName;
    private String receiveImage;


    private CircleImageView imgChatProfile;
    private TextView txtNameChat;
    private TextView txtLastSeenChat;
    private ImageButton backToMainChat;

    private FloatingActionButton btnSendMessageChat;

    private EditText edtTypeMessageChat;



    private DatabaseReference UserRef;

    private DatabaseReference rootRef;
    private final List<MessageChat> messageChats = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageChatAdapter messageChatAdapter;
    private RecyclerView recMessagesChat;

    private DatabaseReference reference;

    private FirebaseAuth firebaseAuth;


    private FirebaseUser firebaseUser;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_chat);

        reference = FirebaseDatabase.getInstance().getReference();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        messageReceiverID = getIntent().getExtras().get("userid").toString();
        receiveName = getIntent().getExtras().get("name").toString();
        receiveImage = getIntent().getExtras().get("image").toString();


        rootRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();


        messageSenderID = firebaseAuth.getCurrentUser().getUid();


        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.custom_toolbar_chat, null);
        mActionBar.setCustomView(customView);
        mActionBar.setDisplayShowCustomEnabled(true);
        backToMainChat = customView.findViewById(R.id.backToMainChat);
        txtNameChat = customView.findViewById(R.id.txtNameChat);
        imgChatProfile = customView.findViewById(R.id.imgChatProfile);

        btnSendMessageChat = findViewById(R.id.btnSendMessageChat);
        edtTypeMessageChat = findViewById(R.id.edtTypeMessageChat);


        messageChatAdapter = new MessageChatAdapter(messageChats);
        recMessagesChat = findViewById(R.id.recMessagesChat);
        linearLayoutManager = new LinearLayoutManager(this);
        recMessagesChat.setLayoutManager(linearLayoutManager);
        recMessagesChat.setAdapter(messageChatAdapter);


        txtNameChat.setText(receiveName);

        backToMainChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfilePersonActivity.state = "Chat";
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        UserRef.child(messageReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("image")) {
                    receiveImage = dataSnapshot.child("image").getValue().toString();
                    if (!receiveImage.equals(""))
                        Picasso.get().load(receiveImage).into(imgChatProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnSendMessageChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTypeMessageChat.getWindowToken(), 0);
            }
        });

    }


    private void sendMessage() {

        String messageText = edtTypeMessageChat.getText().toString();

        if (TextUtils.isEmpty(messageText)) {

        } else {
            String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessagerKeyRef = rootRef.child("Messages").child(messageSenderRef)
                    .child(messageReceiverRef).push();

            String messagePushID = userMessagerKeyRef.getKey();


            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    edtTypeMessageChat.setText("");
                }
            });

        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        rootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        messageChats.clear();

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                            MessageChat messageChat = postSnapShot.getValue(MessageChat.class);
                            messageChats.add(messageChat);
                            messageChatAdapter.notifyDataSetChanged();


                        }
                        recMessagesChat.smoothScrollToPosition(recMessagesChat.getAdapter().getItemCount());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


}
