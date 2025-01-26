package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodapp.DatabaseHandler.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText userNameEdit,emailEdit,passwordEdit,confirmPasswordEdit,phnEditText,DobEditText;
    Button signUp;
    TextView loginPage;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_demo);

        db=new DatabaseHandler(this);
        initViews();
        navigateToLogin();
        setupSignUpButtonListener();
    }

    private void initViews(){
        userNameEdit=findViewById(R.id.usernameEditText);
        passwordEdit=findViewById(R.id.passwordEditText);
        emailEdit=findViewById(R.id.emailEditText);
        confirmPasswordEdit=findViewById(R.id.confirmPasswordEditText);
        signUp = findViewById(R.id.signUpButton);
        loginPage=findViewById(R.id.tv_login);
        phnEditText=findViewById(R.id.phnEditText);
        DobEditText=findViewById(R.id.DobEditText);
    }

    private void navigateToLogin(){
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }

    private void setupSignUpButtonListener() {
        signUp.setOnClickListener(view -> {
            String username = userNameEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String confirmPassword = confirmPasswordEdit.getText().toString();
            String phone = phnEditText.getText().toString();
            String DOB = DobEditText.getText().toString();

            if (validateInput(username, email, password, confirmPassword,phone,DOB)) {
                if (!db.checkUser(email)) {
                    db.addUser(username, email, password,phone,DOB);
                    sendUsersData();
                    Toast.makeText(SignUpActivity.this, "Sign Up successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword, String phone,String Dob) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() || Dob.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendUsersData(){
        JSONObject UsersObj = new JSONObject();
        try {
            UsersObj.put("username", userNameEdit.getText().toString());
            UsersObj.put("password",passwordEdit.getText().toString());
            UsersObj.put("email",emailEdit.getText().toString());
            UsersObj.put("phone", phnEditText.getText().toString());
            UsersObj.put("dob",DobEditText.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String url = "http://10.0.2.2:5002/users";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                UsersObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignUpActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}