package com.example.neuroxtools;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Compass extends AppCompatActivity implements SensorEventListener {

    private Button b1;
    private ImageView iv;
    private TextView tv;
    private SensorManager sm;
    private Sensor s;
    private Sensor s2;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean isAccelerometerSet = false;
    private boolean isMagnetometerSet = false;

    private float currentAzimuthDegrees = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campass);

        iv = findViewById(R.id.imgCompassDial);
        tv  = findViewById(R.id.tvHeadingDegrees);
        b1 = findViewById(R.id.btnGameBack);
        b1.setOnClickListener(v -> finish());
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sm != null) {
            s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            s2 = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        if (s == null || s2 == null) {
            Toast.makeText(this, "Your hardware profile lacks necessary orientation sensor modules.", Toast.LENGTH_LONG).show();
            tv.setText("Sensors Missing");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sm != null && s != null && s2 != null) {
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
            sm.registerListener(this, s2, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            java.lang.System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            isAccelerometerSet = true;
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            java.lang.System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            isMagnetometerSet = true;
        }
        if (isAccelerometerSet && isMagnetometerSet) {
            float[] rotationMatrixR = new float[9];
            float[] inclinationMatrixI = new float[9];

            boolean success = SensorManager.getRotationMatrix(rotationMatrixR, inclinationMatrixI, lastAccelerometer, lastMagnetometer);

            if (success) {
                float[] orientationDataArray = new float[3];
                SensorManager.getOrientation(rotationMatrixR, orientationDataArray);
                float azimuthInRadians = orientationDataArray[0];
                float targetAzimuthDegrees = (float) Math.toDegrees(azimuthInRadians);
                targetAzimuthDegrees = (targetAzimuthDegrees + 360f) % 360f;
                RotateAnimation rotateAnim = new RotateAnimation(
                        -currentAzimuthDegrees,
                        -targetAzimuthDegrees,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                rotateAnim.setDuration(250);
                rotateAnim.setFillAfter(true);
                iv.startAnimation(rotateAnim);
                currentAzimuthDegrees = targetAzimuthDegrees;
                updateHeadingLabelText((int) currentAzimuthDegrees);
            }
        }
    }
    private void updateHeadingLabelText(int degrees) {
        String directionLabel = "N";
        if (degrees >= 338 || degrees < 23) directionLabel = "North";
        else if (degrees >= 23 && degrees < 68) directionLabel = "North-East";
        else if (degrees >= 68 && degrees < 113) directionLabel = "East";
        else if (degrees >= 113 && degrees < 158) directionLabel = "South-East";
        else if (degrees >= 158 && degrees < 203) directionLabel = "South";
        else if (degrees >= 203 && degrees < 248) directionLabel = "South-West";
        else if (degrees >= 248 && degrees < 293) directionLabel = "West";
        else if (degrees >= 293 && degrees < 338) directionLabel = "North-West";

        tv.setText(degrees + "° " + directionLabel);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}