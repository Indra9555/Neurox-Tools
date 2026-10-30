package com.example.neuroxtools;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;

public class Bluetooth extends AppCompatActivity {
    Button b1;
    MaterialButton b2;
    TextView tv;
    BluetoothAdapter ba;
    private static final int REQUEST_BLUETOOTH_PERM = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        tv = findViewById(R.id.tvBluetoothStatus);
        b2 = findViewById(R.id.btnToggleBluetooth);
        b1 = findViewById(R.id.btnGameBack);
        ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            tv.setText("Status: Not Supported on Device");
            b2.setEnabled(false);
        } else {
            updateStatusText();
        }
        b1.setOnClickListener(v -> finish());
        b2.setOnClickListener(v -> {
            if (checkPermissions()) {
                performBluetoothToggle();
            } else {
                requestPermissions();
            }
        });
    }
    private void performBluetoothToggle() {
        if (ba == null) return;

        try {
            if (ba.isEnabled()) {
                // Turn Bluetooth OFF
                ba.disable();
                Toast.makeText(this, "Turning off Bluetooth...", Toast.LENGTH_SHORT).show();
                tv.setText("Status: OFF");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBtIntent);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission Error: Enable Bluetooth Connect", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStatusText() {
        if (ba != null) {
            if (ba.isEnabled()) {
                tv.setText("Status: ON");
            } else {
                tv.setText("Status: OFF");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatusText();
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performBluetoothToggle();
            } else {
                Toast.makeText(this, "Bluetooth Permission Denied. Cannot toggle state.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}