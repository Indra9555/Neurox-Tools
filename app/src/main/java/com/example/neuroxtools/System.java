package com.example.neuroxtools;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class System extends AppCompatActivity {

    private Button b1;
    private MaterialButton b2, b3, b4, b5;
    private MaterialButton b6, b7, b8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        b1= findViewById(R.id.btnGameBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b2 = findViewById(R.id.btnBluetooth);
        b3 = findViewById(R.id.btnTorch);
        b4 = findViewById(R.id.btnWifi);
        b5 = findViewById(R.id.btnCall);
        b6 = findViewById(R.id.btnCompass);
        b7 = findViewById(R.id.btnCamera);
        b8 = findViewById(R.id.btnVibrator);

        b2.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Bluetooth.class));
        });

        b3.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Torch.class));
        });

        b4.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Wifi.class));
        });

        b5.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Call.class));
        });

        b6.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Compass.class));
        });

        b7.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Camera.class));
        });


        b8.setOnClickListener(v -> {
            startActivity(new Intent(System.this, Vibrator.class));
        });
    }
}