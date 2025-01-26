package com.example.foodapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import com.google.firebase.FirebaseApp;


public class OTPVetifyActivity extends AppCompatActivity {

    private EditText et_phoneNumber, otpView;
    private Button btn_verify, Otp_verify;
    private CardView otpCard;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_otpvetify);


        initView();
        verifyOtpActionListener();
    }

    private void initView() {
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        otpView = findViewById(R.id.otpView);
        btn_verify = findViewById(R.id.btn_verify);
        Otp_verify = findViewById(R.id.Otp_verify);
        otpCard = findViewById(R.id.otpCard);
    }

    private void verifyOtpActionListener() {
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = et_phoneNumber.getText().toString().trim();
                if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                    et_phoneNumber.setError("Enter a valid phone number");
                    et_phoneNumber.requestFocus();
                    return;
                }
                sendOtp(phoneNumber);
            }
        });

        Otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otpView.getText().toString().trim();
                if (code.isEmpty() || code.length() < 4) {
                    otpView.setError("Enter valid OTP");
                    otpView.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+1" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // Auto-retrieval or instant verification
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPVetifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                verificationId = verId;
                                otpCard.setVisibility(View.VISIBLE);
                                Toast.makeText(OTPVetifyActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build());
    }

    void sendOtp(String phoneNumber){
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPVetifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = s;
                                otpCard.setVisibility(View.VISIBLE);
                                Toast.makeText(OTPVetifyActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();

                            }
                        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(OTPVetifyActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OTPVetifyActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
