package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.Adapters.InsertNearlyTestCourseAdapter;
import com.example.schoolapp.Models.Entities.LastNews;
import com.example.schoolapp.R;
import com.example.schoolapp.SendNotification.APIService;
import com.example.schoolapp.SendNotification.Client;
import com.example.schoolapp.SendNotification.Data;
import com.example.schoolapp.SendNotification.MyResponse;
import com.example.schoolapp.SendNotification.NotificationSender;
import com.example.schoolapp.SendNotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePersonActivity extends AppCompatActivity {

    private CircleImageView cirProfilePersonImage;
    private TextView txtProfilePersonName;
    private TextView txtProfilePersonStatus;
    private Button btnSendRequest;
    private Button btnCancelRequest;
    private String receiverUserID;

    private String senderUserID;
    private String sendUserName;
    private String currentState;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference chatRequestRef;
    private DatabaseReference contactsRef;
    private DatabaseReference userRef;

    private String UrlImage = "";

    private APIService apiService;

    public static String state = "";

    private ImageButton btnToMainFromProfile;


    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_person);


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");


        firebaseAuth = FirebaseAuth.getInstance();
        senderUserID = firebaseAuth.getCurrentUser().getUid();


        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(senderUserID).child("name");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendUserName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnToMainFromProfile = findViewById(R.id.btnToMainFromProfile);

        btnSendRequest = findViewById(R.id.btnSendRequest);
        btnCancelRequest = findViewById(R.id.btnCancelRequest);

        cirProfilePersonImage = findViewById(R.id.cirProfilePersonImage);
        txtProfilePersonName = findViewById(R.id.txtProfilePersonName);
        txtProfilePersonStatus = findViewById(R.id.txtProfilePersonStatus);


        txtProfilePersonName.setText(getIntent().getExtras().get("name").toString());

        txtProfilePersonStatus.setText(getIntent().getExtras().get("status").toString());


        receiverUserID = getIntent().getExtras().get("userid").toString();


        UrlImage = getIntent().getExtras().get("image").toString();

        if (!UrlImage.equals(""))
            Picasso.get().load(UrlImage).into(cirProfilePersonImage);

        currentState = "new";

        ManageChatRequest();


        btnToMainFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ProfilePersonActivity.state.equals("Friend")) {
                    Intent intent = new Intent(ProfilePersonActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (ProfilePersonActivity.state.equals("FindFriend")) {
                    Intent intent = new Intent(ProfilePersonActivity.this, FindFriendsActivity.class);
                    startActivity(intent);
                }


            }
        });
        //   updateToken();
    }

    private void ManageChatRequest() {

        chatRequestRef.child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverUserID)) {

                            String request_type = dataSnapshot.child(receiverUserID).child("request_type")
                                    .getValue().toString();
                            if (request_type.equals("sent")) {
                                currentState = "request_sent";
                                btnSendRequest.setText(R.string.btnCancelRequest);
                            } else if (request_type.equals("received")) {
                                currentState = "request_received";
                                btnSendRequest.setText(R.string.accept_request);
                                btnCancelRequest.setVisibility(View.VISIBLE);
                                btnCancelRequest.setEnabled(true);
                                btnCancelRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cancelChatRequest();
                                    }
                                });


                            }

                        } else {
                            contactsRef.child(senderUserID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiverUserID)) {
                                                currentState = "friends";
                                                btnSendRequest.setText(R.string.remove_this_contact);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        if (!senderUserID.equals(receiverUserID)) {
            btnSendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSendRequest.setEnabled(false);
                    if (currentState.equals("new")) {
                        sendChatRequest();

                    }
                    if (currentState.equals("request_sent")) {
                        cancelChatRequest();
                    }
                    if (currentState.equals("request_received")) {
                        acceptChatRequest();
                    }
                    if (currentState.equals("friends")) {
                        removeContact();
                    }

                }
            });
        } else {
            btnSendRequest.setVisibility(View.GONE);
        }
    }

    private void removeContact() {


        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePersonActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        contactsRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            contactsRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                btnSendRequest.setEnabled(true);
                                                currentState = "new";
                                                btnSendRequest.setText(R.string.btnSendRequest);

                                                btnCancelRequest.setEnabled(false);
                                                btnCancelRequest.setVisibility(View.GONE);


                                            }
                                        }
                                    });
                        }

                    }
                });


    }

    private void acceptChatRequest() {
        contactsRef.child(senderUserID).child(receiverUserID)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            contactsRef.child(receiverUserID).child(senderUserID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                chatRequestRef.child(senderUserID).child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    chatRequestRef.child(receiverUserID).child(senderUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    btnSendRequest.setEnabled(true);
                                                                                    currentState = "friends";
                                                                                    btnSendRequest.setText(R.string.remove_this_contact);
                                                                                    btnCancelRequest.setVisibility(View.GONE);

                                                                                }
                                                                            });
                                                                }

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelChatRequest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePersonActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        chatRequestRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(receiverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                btnSendRequest.setEnabled(true);
                                                currentState = "new";
                                                btnSendRequest.setText(R.string.btnSendRequest);

                                                btnCancelRequest.setEnabled(false);
                                                btnCancelRequest.setVisibility(View.GONE);


                                            }
                                        }
                                    });
                        }

                    }
                });
    }

    private void sendChatRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePersonActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        chatRequestRef.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            chatRequestRef.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiverUserID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String userToken = dataSnapshot.getValue(String.class);
                                                        sendNotifications(userToken, sendUserName, "Sent Request for you");

                                                        String saveCurrentTime;
                                                        String saveCurrentDate;

                                                        Calendar calendar = Calendar.getInstance();

                                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                        saveCurrentDate = currentDate.format(calendar.getTime());

                                                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                                        saveCurrentTime = currentTime.format(calendar.getTime());

                                                        LastNews lastNews = new LastNews("There is a friend request", sendUserName
                                                                , saveCurrentDate + "   " + saveCurrentTime);

                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserID).child("LastNews")
                                                                .child(FirebaseDatabase.getInstance().getReference().push().getKey())
                                                                .setValue(lastNews);

                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(receiverUserID).child("notification")
                                                                .setValue("online");


                                                        dialog.dismiss();
                                                        btnSendRequest.setEnabled(true);
                                                        currentState = "request_sent";
                                                        btnSendRequest.setText(R.string.btnCancelRequest);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                        }
                                    });

                        }
                    }
                });
        updateToken();
    }


    public void updateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);

    }


    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message, "");
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                if (response.code() == 200) {
                    if (response.body().success != 1) {

                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }


}
