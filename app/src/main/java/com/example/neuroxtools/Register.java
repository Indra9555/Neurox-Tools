package com.example.neuroxtools;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
public class Register extends AppCompatActivity {

    private EditText e1, e2, e3;
    private Button b1;
    private TextView tv;
    private ProgressBar pb;
    private FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fa= FirebaseAuth.getInstance();
        e1 = findViewById(R.id.etEmail);
        e2 = findViewById(R.id.etPassword);
        e3 = findViewById(R.id.etConfirmPassword);
        b1 = findViewById(R.id.btnRegister);
        tv = findViewById(R.id.tvGoToLogin);
        pb = findViewById(R.id.progressBar);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String email = e1.getText().toString().trim();
        String password = e2.getText().toString().trim();
        String confirmPassword = e3.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            e1.setError("Email cannot be empty");
            e1.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            e2.setError("Password cannot be empty");
            e2.requestFocus();
            return;
        }
        if (password.length() < 6) {
            e2.setError("Password must be at least 6 characters");
            e2.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            e3.setError("Passwords do not match");
            e3.requestFocus();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        b1.setEnabled(false);
        fa.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    pb.setVisibility(View.GONE);
                    b1.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        Toast.makeText(Register.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}