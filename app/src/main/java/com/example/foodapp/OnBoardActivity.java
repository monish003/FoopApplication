package com.example.foodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnBoardActivity extends AppCompatActivity {

    private ImageView onBoardImg;
    private TextView onBoardTitle, onBoardDescription;
    private View dot1, dot2, dot3;
    private Button nextButton;
    private int currentPage = 0;
    private static final String PREFS_NAME = "OnboardingPrefs";
    private static final String KEY_FIRST_LAUNCH = "firstLaunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding1);

        if (!isFirstLaunch()) {
            launchMainActivity();
            finish();
            return;
        }

        initViews();
        updateOnboardingScreen();
        nextButtonActionListener();
    }

    private void initViews(){
        onBoardImg = findViewById(R.id.img_chef1);
        onBoardTitle = findViewById(R.id.tv_title);
        onBoardDescription = findViewById(R.id.tv_description);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        nextButton = findViewById(R.id.button_next);
    }

    private void nextButtonActionListener(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                if(currentPage<3){
                    updateOnboardingScreen();
                }
                else{
                    markOnboardingCompleted();
                    launchMainActivity();
                }
            }
        });
    }

    private void updateOnboardingScreen() {
        switch (currentPage) {
            case 0:
                onBoardImg.setImageResource(R.drawable.on_board2);
                onBoardTitle.setText("Fast Delivery");
                onBoardDescription.setText("Your package will be delivered swiftly and safely.");
                updateDots(dot1);
                break;

            case 1:
                onBoardImg.setImageResource(R.drawable.on_board4);
                onBoardTitle.setText("Secure Payment");
                onBoardDescription.setText("Your payment information is safe with us.");
                updateDots(dot2);
                break;

            case 2:
                onBoardImg.setImageResource(R.drawable.on_board5);
                onBoardTitle.setText("24/7 Support");
                onBoardDescription.setText("We're here to help anytime, anywhere.");
                updateDots(dot3);
                nextButton.setText("Finish");
                break;
        }
    }

    private void updateDots(View activeDot) {
        dot1.setBackgroundResource(R.drawable.dot_inactive);
        dot2.setBackgroundResource(R.drawable.dot_inactive);
        dot3.setBackgroundResource(R.drawable.dot_inactive);
        activeDot.setBackgroundResource(R.drawable.dot_active);
    }

    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    private void markOnboardingCompleted() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_LAUNCH, false);
        editor.apply();
    }
    private void launchMainActivity() {
        startActivity(new Intent(OnBoardActivity.this, LoginActivity.class));
        finish();
    }
}