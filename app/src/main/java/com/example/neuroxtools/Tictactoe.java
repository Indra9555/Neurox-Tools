package com.example.neuroxtools;



import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Tictactoe extends AppCompatActivity {
    Button b1;
    TextView tvX, tvO;
    MaterialButton b2;
    MaterialButton[] boardButtons = new MaterialButton[9];
    boolean isPlayerXTurn = true;
    int moveCount = 0;
    int scoreX = 0;
    int scoreO = 0;
    boolean isGameActive = true;
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    final int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);
        b1 = findViewById(R.id.btnGameBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvX = findViewById(R.id.tvScoreX);
        tvO = findViewById(R.id.tvScoreO);
        b2 = findViewById(R.id.btnReset);
        for (int i = 0; i < 9; i++) {
            int resId = getResources().getIdentifier("btn_" + i, "id", getPackageName());
            boardButtons[i] = findViewById(resId);

            final int buttonIndex = i;
            boardButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGridButtonClick(boardButtons[buttonIndex], buttonIndex);
                }
            });
        }


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
    }


    private void onGridButtonClick(MaterialButton tappedButton, int index) {
        if (!isGameActive || gameState[index] != 0) {
            return;
        }

        moveCount++;

        if (isPlayerXTurn) {
            gameState[index] = 1;
            tappedButton.setText("X");
            tappedButton.setTextColor(Color.parseColor("#00D2FF"));
            isPlayerXTurn = false;
        } else {
            gameState[index] = 2;
            tappedButton.setText("O");
            tappedButton.setTextColor(Color.parseColor("#FF3B30"));
            isPlayerXTurn = true;
        }


        if (checkWin()) {
            isGameActive = false;
        } else if (moveCount == 9) {
            isGameActive = false;
            Toast.makeText(this, "It's a Draw!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkWin() {
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 0) {
                String winnerLabel;
                if (gameState[winPosition[0]] == 1) {
                    winnerLabel = "Player X Wins!";
                    scoreX++;
                    tvX.setText(String.valueOf(scoreX));
                } else {
                    winnerLabel = "Player O Wins!";
                    scoreO++;
                    tvO.setText(String.valueOf(scoreO));
                }

                Toast.makeText(this, winnerLabel, Toast.LENGTH_LONG).show();


                highlightWinningLine(winPosition);
                return true;
            }
        }
        return false;
    }

    private void highlightWinningLine(int[] positions) {
        for (int index : positions) {
            boardButtons[index].setBackgroundColor(Color.parseColor("#1B5E20")); // Dark green background shadow
        }
    }
    private void resetBoard() {

        moveCount = 0;
        isPlayerXTurn = true;
        isGameActive = true;


        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 0;
            boardButtons[i].setText("");

            boardButtons[i].setBackgroundColor(Color.parseColor("#0A0F1D"));
        }
    }
}