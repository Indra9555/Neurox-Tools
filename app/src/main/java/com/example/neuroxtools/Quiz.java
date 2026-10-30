package com.example.neuroxtools;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout panelQuizGame, panelQuizScore;
    private TextView tvQuestionProgress, tvQuestionText, tvFinalScore;
    private MaterialButton btnOptA, btnOptB, btnOptC, btnOptD;
    private final String[] questionsList = {
            "What is the National Animal of India?",
            "What is the capital city of India?",
            "What is the currency of India?",
            "What is the National Fruit of India?",
            "In which year did India get Independence?"
    };

    private final String[][] optionsList = {
            {"Lion", "Tiger", "Leopard", "Elephant"},
            {"Mumbai", "Kolkata", "New Delhi", "Chennai"},
            {"Dollar", "Rupee", "Euro", "Yen"},
            {"Apple", "Banana", "Grapes", "Mango"},
            {"1942", "1945", "1947", "1950"}
    };

    private final String[] correctAnswersList = {
            "Tiger",
            "New Delhi",
            "Rupee",
            "Mango",
            "1947"
    };

    private int activeQuestionIndex = 0;
    private int scoreCounter = 0;
    private boolean isOptionClickLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        panelQuizGame = findViewById(R.id.panelQuizGame);
        panelQuizScore = findViewById(R.id.panelQuizScore);
        tvQuestionProgress = findViewById(R.id.tvQuestionProgress);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        tvFinalScore = findViewById(R.id.tvFinalScore);

        btnOptA = findViewById(R.id.btnOptionA);
        btnOptB = findViewById(R.id.btnOptionB);
        btnOptC = findViewById(R.id.btnOptionC);
        btnOptD = findViewById(R.id.btnOptionD);

        MaterialButton btnQuizBack = findViewById(R.id.btnQuizBack);
        MaterialButton btnRestartQuiz = findViewById(R.id.btnRestartQuiz);
        MaterialButton btnExitToHub = findViewById(R.id.btnExitToHub);
        btnOptA.setOnClickListener(this);
        btnOptB.setOnClickListener(this);
        btnOptC.setOnClickListener(this);
        btnOptD.setOnClickListener(this);

        if (btnQuizBack != null) btnQuizBack.setOnClickListener(v -> exitToDashboard());
        if (btnExitToHub != null) btnExitToHub.setOnClickListener(v -> exitToDashboard());

        if (btnRestartQuiz != null) {
            btnRestartQuiz.setOnClickListener(v -> resetQuizState());
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                exitToDashboard();
            }
        });
        renderQuestionBlock();
    }

    private void renderQuestionBlock() {
        if (activeQuestionIndex >= questionsList.length) {
            displayFinalScorePanel();
            return;
        }

        isOptionClickLocked = false;
        resetButtonStyles();
        tvQuestionProgress.setText("Question " + (activeQuestionIndex + 1) + "/" + questionsList.length);
        tvQuestionText.setText(questionsList[activeQuestionIndex]);

        btnOptA.setText(optionsList[activeQuestionIndex][0]);
        btnOptB.setText(optionsList[activeQuestionIndex][1]);
        btnOptC.setText(optionsList[activeQuestionIndex][2]);
        btnOptD.setText(optionsList[activeQuestionIndex][3]);
    }

    @Override
    public void onClick(View view) {
        if (isOptionClickLocked) return;
        isOptionClickLocked = true;

        MaterialButton clickedButton = (MaterialButton) view;
        String chosenAnswerText = clickedButton.getText().toString();
        String correctSignatureText = correctAnswersList[activeQuestionIndex];

        if (chosenAnswerText.equals(correctSignatureText)) {
            scoreCounter++;
            clickedButton.setBackgroundColor(Color.parseColor("#2E7D32"));
            clickedButton.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            clickedButton.setBackgroundColor(Color.parseColor("#C62828"));
            clickedButton.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#F44336")));
            highlightCorrectOption(correctSignatureText);
        }
        new Handler().postDelayed(() -> {
            activeQuestionIndex++;
            renderQuestionBlock();
        }, 1200);
    }

    private void highlightCorrectOption(String correctAnswer) {
        if (btnOptA.getText().toString().equals(correctAnswer)) setGreenStroke(btnOptA);
        else if (btnOptB.getText().toString().equals(correctAnswer)) setGreenStroke(btnOptB);
        else if (btnOptC.getText().toString().equals(correctAnswer)) setGreenStroke(btnOptC);
        else if (btnOptD.getText().toString().equals(correctAnswer)) setGreenStroke(btnOptD);
    }

    private void setGreenStroke(MaterialButton button) {
        button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        button.setStrokeWidth(3);
    }

    private void resetButtonStyles() {
        int defaultBgColor = Color.parseColor("#1E293B");
        int defaultStrokeColor = Color.parseColor("#334155");

        MaterialButton[] buttons = {btnOptA, btnOptB, btnOptC, btnOptD};
        for (MaterialButton btn : buttons) {
            btn.setBackgroundColor(defaultBgColor);
            btn.setStrokeColor(ColorStateList.valueOf(defaultStrokeColor));
            btn.setStrokeWidth(1);
        }
    }

    private void displayFinalScorePanel() {
        panelQuizGame.setVisibility(View.GONE);
        panelQuizScore.setVisibility(View.VISIBLE);
        tvFinalScore.setText(scoreCounter + "/" + questionsList.length);
    }

    private void resetQuizState() {
        scoreCounter = 0;
        activeQuestionIndex = 0;
        panelQuizScore.setVisibility(View.GONE);
        panelQuizGame.setVisibility(View.VISIBLE);
        renderQuestionBlock();
    }

    private void exitToDashboard() {
        Intent intent = new Intent(Quiz.this, Application.class);
        startActivity(intent);
        finish();
    }
}