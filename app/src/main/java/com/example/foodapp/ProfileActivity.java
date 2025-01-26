package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodapp.DatabaseHandler.DatabaseHandler;

public class ProfileActivity extends AppCompatActivity {

    TextView tv_Logout,tv_logoutArrow,tv_profileName,tv_profileMail,tv_manageAccount,tv_manageAccountArrow,tv_termNcondition,tv_termNconditionArrow;
    DatabaseHandler db = new DatabaseHandler(ProfileActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        bindUserDetails();
        logoutClickActionListener();
        manageAccountClickActionListener();
        termsConditionClickActionListener();
    }

    private void initViews(){
        tv_Logout = findViewById(R.id.tv_Logout);
        tv_logoutArrow = findViewById(R.id.tv_logoutArrow);
        tv_profileName = findViewById(R.id.tv_profileName);
        tv_profileMail = findViewById(R.id.tv_profileMail);
        tv_manageAccount = findViewById(R.id.tv_manageAccount);
        tv_manageAccountArrow = findViewById(R.id.tv_manageAccountArrow);
        tv_termNcondition = findViewById(R.id.tv_termNcondition);
        tv_termNconditionArrow = findViewById(R.id.tv_termNconditionArrow);
    }

    private void logoutClickActionListener(){
        tv_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        tv_logoutArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }

    private void manageAccountClickActionListener(){
        tv_manageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Still under development", Toast.LENGTH_SHORT).show();
            }
        });

        tv_manageAccountArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Still under development", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void termsConditionClickActionListener(){
        tv_termNcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Still under development", Toast.LENGTH_SHORT).show();
            }
        });

        tv_termNconditionArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Still under development", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindUserDetails(){
        tv_profileName.setText(db.getLatestUsername());
        tv_profileMail.setText(db.getLatestPhone() + " . " + db.getLatestEmail());
    }
}