package com.example.neuroxtools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGameView extends View {

    final int GRID_WIDTH = 20;
    final int GRID_HEIGHT = 30;
    int cellSize;

    final ArrayList<Point> snake = new ArrayList<>();
     Point apple;

    enum Direction { UP, DOWN, LEFT, RIGHT }
    Direction currentDirection = Direction.RIGHT;

    final Paint snakePaint = new Paint();
    final Paint applePaint = new Paint();

    boolean isGameOver = false;
    int score = 0;
    final OnScoreChangeListener scoreListener;

    public interface OnScoreChangeListener {
        void onScoreChanged(int score);
        void onGameOver();
    }

    public SnakeGameView(Context context, OnScoreChangeListener listener) {
        super(context);
        this.scoreListener = listener;
        snakePaint.setColor(0xFF00E676);
        snakePaint.setStyle(Paint.Style.FILL);

        applePaint.setColor(0xFFFF3B30);
        applePaint.setStyle(Paint.Style.FILL);

        resetGame();
    }

    public void resetGame() {
        snake.clear();
        snake.add(new Point(10, 15));
        snake.add(new Point(9, 15));
        snake.add(new Point(8, 15));

        currentDirection = Direction.RIGHT;
        score = 0;
        isGameOver = false;

        spawnApple();
        invalidate();
    }

    boolean getIsGameOver() {
        return isGameOver;
    }

    void spawnApple() {
        Random random = new Random();
        int x, y;
        boolean onSnake;

        do {
            x = random.nextInt(GRID_WIDTH);
            y = random.nextInt(GRID_HEIGHT);
            onSnake = false;
            for (Point segment : snake) {
                if (segment.x == x && segment.y == y) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);

        apple = new Point(x, y);
    }

    public void setDirection(Direction direction) {
        if (direction == Direction.UP && currentDirection != Direction.DOWN) currentDirection = direction;
        if (direction == Direction.DOWN && currentDirection != Direction.UP) currentDirection = direction;
        if (direction == Direction.LEFT && currentDirection != Direction.RIGHT) currentDirection = direction;
        if (direction == Direction.RIGHT && currentDirection != Direction.LEFT) currentDirection = direction;
    }

    public void updateGame() {
        if (isGameOver) return;

        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (currentDirection) {
            case UP:    newHead.y--; break;
            case DOWN:  newHead.y++; break;
            case LEFT:  newHead.x--; break;
            case RIGHT: newHead.x++; break;
        }
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || newHead.y < 0 || newHead.y >= GRID_HEIGHT) {
            endGame();
            return;
        }
        for (Point segment : snake) {
            if (newHead.x == segment.x && newHead.y == segment.y) {
                endGame();
                return;
            }
        }

        snake.add(0, newHead);
        if (apple != null && newHead.x == apple.x && newHead.y == apple.y) {
            score += 10;
            if (scoreListener != null) scoreListener.onScoreChanged(score);
            spawnApple();
        } else {
            if (!snake.isEmpty()) {
                snake.remove(snake.size() - 1);
            }
        }

        invalidate();
    }

     void endGame() {
        isGameOver = true;
        if (scoreListener != null) {
            scoreListener.onGameOver();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() == 0 || getHeight() == 0) return;
        cellSize = Math.min(getWidth() / GRID_WIDTH, getHeight() / GRID_HEIGHT);

        int offsetX = (getWidth() - (GRID_WIDTH * cellSize)) / 2;
        int offsetY = (getHeight() - (GRID_HEIGHT * cellSize)) / 2;
        if (apple != null) {
            canvas.drawRect(
                    offsetX + apple.x * cellSize + 2,
                    offsetY + apple.y * cellSize + 2,
                    offsetX + (apple.x + 1) * cellSize - 2,
                    offsetY + (apple.y + 1) * cellSize - 2,
                    applePaint
            );
        }
        for (Point segment : snake) {
            canvas.drawRect(
                    offsetX + segment.x * cellSize + 1,
                    offsetY + segment.y * cellSize + 1,
                    offsetX + (segment.x + 1) * cellSize - 1,
                    offsetY + (segment.y + 1) * cellSize - 1,
                    snakePaint
            );
        }
    }
}
