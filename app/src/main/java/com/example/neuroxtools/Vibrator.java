package com.example.neuroxtools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Vibrator extends AppCompatActivity {
    TextView tv;
    MaterialButton mb;
    android.os.Vibrator vibratorEngine;
    boolean isVibrating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrator);

        tv = findViewById(R.id.tvStatus);
        mb= findViewById(R.id.btnToggleVibration);
        MaterialButton btnBack = findViewById(R.id.btnVibratorBack);
        if (tv == null) {
            Toast.makeText(this, "Layout Error: tvStatus not found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (mb == null) {
            Toast.makeText(this, "Layout Error: btnToggleVibration not found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (btnBack == null) {
            Toast.makeText(this, "Layout Error: btnVibratorBack not found!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(v -> finish());
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                if (vibratorManager != null) {
                    vibratorEngine = vibratorManager.getDefaultVibrator();
                }
            } else {
                vibratorEngine = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to connect to hardware motor service.", Toast.LENGTH_SHORT).show();
        }
        mb.setOnClickListener(v -> {
            if (vibratorEngine == null || !vibratorEngine.hasVibrator()) {
                Toast.makeText(this, "Vibration motor hardware is missing or unsupported!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isVibrating) {
                stopMassager();
            } else {
                startMassager();
            }
        });
    }

    private void startMassager() {
        isVibrating = true;
        tv.setText("STATUS: RUNNING");
        tv.setTextColor(0xFF00D2FF);

        mb.setText("STOP");
        mb.setBackgroundTintList(ColorStateList.valueOf(0xFFFF3B30));
        mb.setTextColor(0xFFFFFFFF);

        long[] patternTimings = {0, 500, 200, 500};
        int[] patternAmplitudes = {0, 255, 0, 255};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createWaveform(patternTimings, patternAmplitudes, 0);
            vibratorEngine.vibrate(effect);
        } else {
            vibratorEngine.vibrate(patternTimings, 0);
        }
    }

    private void stopMassager() {
        isVibrating = false;
        tv.setText("STATUS: OFF");
         tv.setTextColor(0xFF64748B);

        mb.setText("START");
        mb.setBackgroundTintList(ColorStateList.valueOf(0xFF00E676));
        mb.setTextColor(0xFF0A0F1D);

        if (vibratorEngine != null) {
            vibratorEngine.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isVibrating) {
            stopMassager();
        }
    }
}