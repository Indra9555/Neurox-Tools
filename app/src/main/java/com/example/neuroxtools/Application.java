package com.example.neuroxtools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Application extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        MaterialButton btnCalculator = findViewById(R.id.btnCalculator);
        MaterialButton btnMediaPlayer = findViewById(R.id.btnMediaPlayer);
        MaterialButton btnVideoPlayer = findViewById(R.id.btnVideoPlayer);
        MaterialButton btnWebBrowser = findViewById(R.id.btnWebBrowser);
        MaterialButton btnQuiz = findViewById(R.id.btnQuiz);
        Button btnAppBack = findViewById(R.id.btnAppBack);
        if (btnAppBack != null) {
            btnAppBack.setOnClickListener(v -> finish());
        }
        if (btnCalculator != null) {
            btnCalculator.setOnClickListener(v -> {
                startActivity(new Intent(Application.this, com.example.neuroxtools.Calculator.class));
            });
        }

        if (btnMediaPlayer != null) {
            btnMediaPlayer.setOnClickListener(v -> {
                startActivity(new Intent(Application.this, com.example.neuroxtools.MediaPlayer.class));
            });
        }

        if (btnVideoPlayer != null) {
            btnVideoPlayer.setOnClickListener(v -> {
                startActivity(new Intent(Application.this, com.example.neuroxtools.VideoPlayer.class));
            });
        }

        if (btnWebBrowser != null) {
            btnWebBrowser.setOnClickListener(v -> {
                startActivity(new Intent(Application.this, com.example.neuroxtools.WebBrowser.class));
            });
        }

        if (btnQuiz != null) {
            btnQuiz.setOnClickListener(v -> {
                startActivity(new Intent(Application.this, com.example.neuroxtools.Quiz.class));
            });
        }
    }
}