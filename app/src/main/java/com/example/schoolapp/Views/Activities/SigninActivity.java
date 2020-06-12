package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolapp.R;
import com.example.schoolapp.SendNotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SigninActivity extends AppCompatActivity {


    private Button btnLogin, btnLoginUsePhone;
    private TextView SignUp;
    private EditText editTextUsernameLogin;
    private EditText editTextPasswordLogin;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser firebaseUser;

    private DatabaseReference UserRef;


    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        editTextUsernameLogin = findViewById(R.id.editTextUsernameLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginUsePhone = findViewById(R.id.btnLoginUsePhone);

        SignUp = findViewById(R.id.SignUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });


        btnLoginUsePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void AllowUserToLogin() {

        String Email = editTextUsernameLogin.getText().toString();
        String Password = editTextPasswordLogin.getText().toString();

        if (Email.isEmpty()) {
            editTextUsernameLogin.setError("Email Requierd");
            editTextUsernameLogin.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            editTextUsernameLogin.setError("Vaild Email Requierd");
            editTextUsernameLogin.requestFocus();
            return;
        }

        if (Password.isEmpty() || Password.length() < 6) {
            editTextPasswordLogin.setError("6 char password required");
            editTextPasswordLogin.requestFocus();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();

        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String currentUserID = firebaseAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            UserRef.child(currentUserID).child("device_token")
                                    .setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            toMainActivity();
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SigninActivity.this, message + "", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }

                    }

                });

    }


    @Override
    protected void onStart() {
        super.onStart();


        if(firebaseUser!=null)
        {
            toMainActivity();
        }


    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}
