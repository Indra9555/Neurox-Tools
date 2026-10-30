package com.example.neuroxtools;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Snake extends AppCompatActivity implements SnakeGameView.OnScoreChangeListener {
    SnakeGameView gameView;
    TextView tv;
    Button b1;
    FrameLayout gameContainer;
    private View btnUp, btnDown, btnLeft, btnRight;
    private final Handler gameHandler = new Handler();
    private final int START_SPEED_MS = 450;
    private final int MIN_SPEED_LIMIT_MS = 120;
    private final int SPEED_DECREMENT_MS = 15;
    private int currentSpeedMs = START_SPEED_MS;
    private boolean isLoopRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        tv = findViewById(R.id.tvScore);
        gameContainer = findViewById(R.id.gameContainer);
        gameView = new SnakeGameView(this, this);
        if (gameContainer != null) {
            gameContainer.addView(gameView);
        }
        b1 = findViewById(R.id.btnGameBack);
        if (b1!= null) {
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopGameLoop();
                    finish();
                }
            });
        }
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);

        if (btnUp != null) {
            btnUp.setOnClickListener(v -> gameView.setDirection(SnakeGameView.Direction.UP));
        }
        if (btnDown != null) {
            btnDown.setOnClickListener(v -> gameView.setDirection(SnakeGameView.Direction.DOWN));
        }
        if (btnLeft != null) {
            btnLeft.setOnClickListener(v -> gameView.setDirection(SnakeGameView.Direction.LEFT));
        }
        if (btnRight != null) {
            btnRight.setOnClickListener(v -> gameView.setDirection(SnakeGameView.Direction.RIGHT));
        }
        startGameLoop();
    }
    private final Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            if (gameView != null) {
                gameView.updateGame();
            }
            gameHandler.postDelayed(this, currentSpeedMs);
        }
    };

    private void startGameLoop() {
        if (!isLoopRunning) {
            gameHandler.postDelayed(loopRunnable, currentSpeedMs);
            isLoopRunning = true;
        }
    }

    private void stopGameLoop() {
        gameHandler.removeCallbacks(loopRunnable);
        isLoopRunning = false;
    }

    @Override
    public void onScoreChanged(int score) {
        if (tv != null) {
            tv.setText("SCORE: " + score);
        }
        if (currentSpeedMs > MIN_SPEED_LIMIT_MS) {
            currentSpeedMs -= SPEED_DECREMENT_MS;
        }
    }

    @Override
    public void onGameOver() {
        stopGameLoop();
        Toast.makeText(this, "GAME OVER!", Toast.LENGTH_SHORT).show();
        currentSpeedMs = START_SPEED_MS;

        if (gameView != null) {
            gameView.resetGame();
        }
        if (tv != null) {
            tv.setText("SCORE: 0");
        }
        startGameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGameLoop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null && !gameView.getIsGameOver()) {
            startGameLoop();
        }
    }
}