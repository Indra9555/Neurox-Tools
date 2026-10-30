package com.example.neuroxtools;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Games extends AppCompatActivity {
    Button TicTacToe;
    Button SnakeGame;
    Button TrafficDodger;
    Button BackToServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        TicTacToe = findViewById(R.id.btnTicTacToe);
        SnakeGame = findViewById(R.id.btnSnakeGame);
        TrafficDodger = findViewById(R.id.btnTrafficDodger);
        BackToServices = findViewById(R.id.btnBackToServices);


        TicTacToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Games.this, Tictactoe.class);
                startActivity(intent);
            }
        });
        SnakeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Games.this, Snake.class);
                startActivity(intent);
            }
        });
        TrafficDodger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Games.this, Traffic.class);
                startActivity(intent);
            }
        });
        BackToServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}