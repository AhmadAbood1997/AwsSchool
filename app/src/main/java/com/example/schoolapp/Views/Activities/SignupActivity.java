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

import com.example.schoolapp.Models.Entities.User;
import com.example.schoolapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private TextView HaveAccount;

    private EditText editTextUsernameRegister;
    private EditText editTextPasswordRegister;

    private Button btnRegister;

    private FirebaseAuth firebaseAuth;

    private AlertDialog dialog;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();

        editTextUsernameRegister = findViewById(R.id.editTextUsernameRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        HaveAccount = findViewById(R.id.HaveAccount);
        btnRegister = findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });


        HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);

            }
        });


    }

    private void CreateNewAccount() {
        String Email = editTextUsernameRegister.getText().toString();
        String Password = editTextPasswordRegister.getText().toString();

        if (Email.isEmpty()) {
            editTextUsernameRegister.setError("Email Requierd");
            editTextUsernameRegister.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            editTextUsernameRegister.setError("Vaild Email Requierd");
            editTextUsernameRegister.requestFocus();
            return;
        }

        if (Password.isEmpty() || Password.length() < 6) {
            editTextPasswordRegister.setError("6 char password required");
            editTextPasswordRegister.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        dialog.show();


        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            String currentUserID = firebaseAuth.getCurrentUser().getUid();



                            HashMap<String, Object> profileMap = new HashMap<>();
                            profileMap.put("uid", currentUserID);
                            profileMap.put("name", "");
                            profileMap.put("status", "");
                            profileMap.put("image", "");
                            profileMap.put("device_token", deviceToken);
                            profileMap.put("notification", "offline");




                            reference.child("Users").child(currentUserID)
                                    .setValue(profileMap);











                            toMainActivity();
                            Toast.makeText(SignupActivity.this, "Accont Created Succsessfly...", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SignupActivity.this, message + "", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                });

    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
