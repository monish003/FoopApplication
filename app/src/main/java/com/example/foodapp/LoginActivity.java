package com.example.foodapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.DatabaseHandler.DatabaseHandler;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login,btn_otp;
    TextView tvSignup,tv_loginMsg;
    DatabaseHandler db;


    private String[] messages = {
            "Made with Love - Every Meal Matters",
            "Welcome Users",
            "Enjoy Your Food Journey"
    };

    private int messageIndex = 0;
    private Handler handler = new Handler();
    private Runnable textChanger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo);

        db= new DatabaseHandler(LoginActivity.this);
        initView();
        LoginFuctionality();
        navigateToSignUp();
        startTextAnimation();
        OTPButtonActionListener();
        addednewfunct();
    }
    private void addednewfunct(){
        System.out.println('To check on code change')
    }

    private void initView(){
        username=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        login=findViewById(R.id.btnLogin);
        tvSignup =findViewById(R.id.tvSignup);
        tv_loginMsg=findViewById(R.id.tv_loginMsg);
        btn_otp=findViewById(R.id.btn_otp);
    }

    private void opt(){
        System.out.println('Added function to chcek on code changes')
    }

    private void LoginFuctionality(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateUserLogin()) {
                    String user1 = username.getText().toString();
                    String pass1 = password.getText().toString();

                    DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
                    db.getReadableDatabase();

                    if (db.validateUser(user1, pass1)) {
                        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean ValidateUserLogin() {
        return username.getText().toString().trim().length() > 0 &&
                password.getText().toString().trim().length() > 0;
    }

    private void navigateToSignUp(){
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void startTextAnimation() {
        textChanger = new Runnable() {
            @Override
            public void run() {
                animateTextChange();
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(textChanger);
    }

    private void animateTextChange() {
        tv_loginMsg.setText(messages[messageIndex]);
        messageIndex = (messageIndex + 1) % messages.length;

        ObjectAnimator slideUp = ObjectAnimator.ofFloat(tv_loginMsg, "translationY", 30f, 0f);
        slideUp.setDuration(500);
        slideUp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(textChanger);
    }

    private void OTPButtonActionListener(){
        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,OTPVetifyActivity.class));
            }
        });
    }

}
