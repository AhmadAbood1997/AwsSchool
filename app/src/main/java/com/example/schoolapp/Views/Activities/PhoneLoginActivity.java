package com.example.schoolapp.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schoolapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    Button btnSendCode, btnVerify;
    EditText edtEnterPhone, edtEnterCode;
    AlertDialog dialog;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private FirebaseAuth auth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        btnSendCode = findViewById(R.id.btnSendCode);
        btnVerify = findViewById(R.id.btnVerify);
        edtEnterPhone = findViewById(R.id.edtEnterPhone);
        edtEnterCode = findViewById(R.id.edtEnterCode);


        auth = FirebaseAuth.getInstance();

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = edtEnterPhone.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(PhoneLoginActivity.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLoginActivity.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.loading_dialog);
                    dialog = builder.create();

                    dialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber.trim(),
                            60,
                            TimeUnit.SECONDS,
                            PhoneLoginActivity.this,
                            callbacks


                    );

                }


            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSendCode.setVisibility(View.GONE);
                edtEnterPhone.setVisibility(View.GONE);

                String verificationCode = edtEnterCode.getText().toString();
                if (TextUtils.isEmpty(verificationCode)) {
                    Toast.makeText(PhoneLoginActivity.this, "Please write verification code... ", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLoginActivity.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.loading_dialog);
                    dialog = builder.create();

                    dialog.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                dialog.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();

                btnSendCode.setVisibility(View.VISIBLE);
                edtEnterPhone.setVisibility(View.VISIBLE);

                btnVerify.setVisibility(View.GONE);
                edtEnterCode.setVisibility(View.GONE);


            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                resendingToken = token;

                dialog.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Code has been sent, please check and verify...", Toast.LENGTH_SHORT).show();

                btnSendCode.setVisibility(View.GONE);
                edtEnterPhone.setVisibility(View.GONE);

                btnVerify.setVisibility(View.VISIBLE);
                edtEnterCode.setVisibility(View.VISIBLE);


            }


        };


    }


    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "You're logged in successfully", Toast.LENGTH_SHORT).show();
                            toMainActivity();
                        } else {

                            String message  = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, message+"", Toast.LENGTH_SHORT).show();
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
