package com.example.neuroxtools;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class Traffic extends AppCompatActivity {
     GameView gameView;
     Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        LinearLayout container = findViewById(R.id.gameViewContainer);
        gameView = new GameView(this);
        if (container != null) {
            container.addView(gameView);
        }
        b1 = findViewById(R.id.btnGameBack);
        if (b1 != null) {
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameView != null) {
                        gameView.pauseEngine();
                    }
                    finish();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pauseEngine();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.resumeEngine();
        }
    }
}