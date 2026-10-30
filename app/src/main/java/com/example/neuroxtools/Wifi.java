package com.example.neuroxtools;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Wifi extends AppCompatActivity {

    Button b1;
    MaterialButton b2;
    TextView tv;
    WifiManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        tv= findViewById(R.id.tvWifiStatus);
        b2 = findViewById(R.id.btnToggleWifi);
        b1= findViewById(R.id.btnGameBack);
        wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        updateWifiStatusText();
        b1.setOnClickListener(v -> finish());
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent intent = new Intent(Settings.Panel.ACTION_WIFI);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }
    private void updateWifiStatusText() {
        if (wm!= null) {
            if (wm.isWifiEnabled()) {
                tv.setText("Wi-Fi Status: ENABLED");
            } else {
                tv.setText("Wi-Fi Status: DISABLED");
            }
        } else {
            tv.setText("Wi-Fi Status: Not Supported");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWifiStatusText();
    }
}