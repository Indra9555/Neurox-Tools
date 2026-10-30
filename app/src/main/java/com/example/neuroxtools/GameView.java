package com.example.neuroxtools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean isPlaying;

    Paint paint;
    Canvas canvas;
    int screenWidth, screenHeight;
    float playerX, playerY;
    int playerSize = 80;
    int score = 0;
    boolean isGameOver = false;

    // Obstacle Variables
    ArrayList<EnemyCar> enemyList;
    int spawnTimer = 0;
    int spawnRate = 30;
    float baseSpeed = 15f;
    Random random;
    public GameView(Context context) {
        super(context);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        enemyList = new ArrayList<>();
        random = new Random();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resumeEngine();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pauseEngine();
    }
    @Override
    public void run() {
        while (isPlaying) {
            if (!surfaceHolder.getSurface().isValid()) continue;

            updatePhysics();
            drawCanvas();
            controlFrameRate();
        }
    }

    private void updatePhysics() {
        if (isGameOver) return;
        if (screenWidth == 0) {
            screenWidth = getWidth();
            screenHeight = getHeight();
            playerX = screenWidth / 2f - (playerSize / 2f);
            playerY = screenHeight - playerSize - 100;
        }
        spawnTimer++;
        if (spawnTimer >= spawnRate) {
            spawnTimer = 0;
            if (screenWidth > playerSize) {
                int randomX = random.nextInt(screenWidth - playerSize);
                enemyList.add(new EnemyCar(randomX, 0, baseSpeed + random.nextInt(10)));
            }
        }

        ArrayList<EnemyCar> outOfBoundsList = new ArrayList<>();
        for (EnemyCar enemy : enemyList) {
            enemy.y += enemy.speed;
            if (enemy.y + playerSize >= playerY && enemy.y <= playerY + playerSize) {
                if (enemy.x + playerSize >= playerX && enemy.x <= playerX + playerSize) {
                    isGameOver = true;
                }
            }
            if (enemy.y > screenHeight) {
                outOfBoundsList.add(enemy);
                score++;
                if (score % 5 == 0) {
                    baseSpeed += 2f;
                }
            }
        }
        enemyList.removeAll(outOfBoundsList);
    }

    private void drawCanvas() {
        canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            // Draw Background Grey Road
            canvas.drawColor(Color.parseColor("#4B5563"));
            paint.setColor(Color.WHITE);
            for (int i = 0; i < screenHeight; i += 120) {
                canvas.drawRect(screenWidth / 2f - 10, i, screenWidth / 2f + 10, i + 80, paint);
            }
            paint.setColor(Color.parseColor("#00D2FF"));
            canvas.drawRect(playerX, playerY, playerX + playerSize, playerY + playerSize, paint);
            paint.setColor(Color.parseColor("#FF3B30"));
            for (EnemyCar enemy : enemyList) {
                canvas.drawRect(enemy.x, enemy.y, enemy.x + playerSize, enemy.y + playerSize, paint);
            }
            paint.setColor(Color.WHITE);
            paint.setTextSize(sp(24));
            paint.setFakeBoldText(true);
            canvas.drawText("SCORE: " + score, 50, 100, paint);

            if (isGameOver) {
                paint.setColor(Color.parseColor("#CC0000"));
                paint.setTextSize(sp(44));
                float gameOverX = (screenWidth - paint.measureText("GAME OVER")) / 2f;
                canvas.drawText("GAME OVER", gameOverX, screenHeight / 2f, paint);
                paint.setTextSize(sp(18));
                paint.setColor(Color.WHITE);
                float restartX = (screenWidth - paint.measureText("Tap Screen to Restart")) / 2f;
                canvas.drawText("Tap Screen to Restart", restartX, (screenHeight / 2f) + 80, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void controlFrameRate() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isGameOver) {
                    enemyList.clear();
                    score = 0;
                    baseSpeed = 15f;
                    isGameOver = false;
                    playerX = screenWidth / 2f - (playerSize / 2f);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isGameOver) {
                    playerX = event.getX() - (playerSize / 2f);
                    if (playerX < 0) playerX = 0;
                    if (playerX > screenWidth - playerSize) playerX = screenWidth - playerSize;
                }
                break;
        }
        return true;
    }

    public void resumeEngine() {
        if (isPlaying) return;
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pauseEngine() {
        isPlaying = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread = null;
    }
    private float sp(int value) {
        return value * getResources().getDisplayMetrics().scaledDensity;
    }

    private static class EnemyCar {
        float x, y, speed;
        EnemyCar(float x, float y, float speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }
    }
}