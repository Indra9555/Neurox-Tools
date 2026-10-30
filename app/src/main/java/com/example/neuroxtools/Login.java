package com.example.neuroxtools;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
     EditText e1, e2;
     Button b1;
     ImageButton b2;
     ProgressBar pb;
    FirebaseAuth fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fa = FirebaseAuth.getInstance();
        e1 = findViewById(R.id.etEmail);
        e2 = findViewById(R.id.etPassword);
        b1 = findViewById(R.id.btnLogin);
        b2 = findViewById(R.id.btnBack);
        pb = findViewById(R.id.progressBar);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = e1.getText().toString().trim();
        String password = e2.getText().toString().trim();
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
        pb.setVisibility(View.VISIBLE);
        b1.setEnabled(false);
        fa.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    pb.setVisibility(View.GONE);
                    b1.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Services.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Invalid user or password. Please try again.", Toast.LENGTH_LONG).show();
                        e2.setText("");
                    }
                });
    }
}