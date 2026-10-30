package com.example.neuroxtools;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Torch extends AppCompatActivity {

    Button b1;
    MaterialButton b2;
    TextView tv;
    boolean isTorchOn = false;
    CameraManager cm;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);

        tv = findViewById(R.id.tvTorchStatus);
        b2 = findViewById(R.id.btnToggleTorch);
        b1 = findViewById(R.id.btnGameBack);
        cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (cm != null && cm.getCameraIdList().length > 0) {
                cameraId = cm.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to access camera hardware.", Toast.LENGTH_SHORT).show();
        }
        b1.setOnClickListener(v -> finish());
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashlight();
            }
        });
    }
    private void toggleFlashlight() {
        if (cm == null || cameraId == null) {
            Toast.makeText(this, "Flashlight hardware unavailable on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!isTorchOn) {
                cm.setTorchMode(cameraId, true);
                isTorchOn = true;
                tv.setText("Torch: ON");
                b2.setText("TURN OFF");
                b2.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FF3B30"))); // Red alert shade when active
            } else {
                cm.setTorchMode(cameraId, false);
                isTorchOn = false;
                tv.setText("Torch: OFF");
                b2.setText("TURN ON");
                b2.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1E293B"))); // Default slate tint
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Camera is currently busy or being used by another app.", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "This device does not contain a physical flash unit.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isTorchOn && cm != null && cameraId != null) {
            try {
                cm.setTorchMode(cameraId, false);
                isTorchOn = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}