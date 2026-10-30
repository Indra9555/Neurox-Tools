package com.example.neuroxtools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Call extends AppCompatActivity {

    Button b1;
    EditText e1;
    ImageButton ib;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        e1= findViewById(R.id.etPhoneNumber);
        ib  = findViewById(R.id.btnBackspace);
        fb = findViewById(R.id.btnMakeCall);
        b1= findViewById(R.id.btnGameBack);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupKeypadButton(R.id.btn0, "0");
        setupKeypadButton(R.id.btn1, "1");
        setupKeypadButton(R.id.btn2, "2");
        setupKeypadButton(R.id.btn3, "3");
        setupKeypadButton(R.id.btn4, "4");
        setupKeypadButton(R.id.btn5, "5");
        setupKeypadButton(R.id.btn6, "6");
        setupKeypadButton(R.id.btn7, "7");
        setupKeypadButton(R.id.btn8, "8");
        setupKeypadButton(R.id.btn9, "9");
        setupKeypadButton(R.id.btnStar, "*");
        setupKeypadButton(R.id.btnHash, "#");

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = e1.getText().toString();
                if (currentText.length() > 0) {
                    // Remove the very last digit typed
                    e1.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });
        ib.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                e1.setText("");
                return true;
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dynamicNumber = e1.getText().toString().trim();

                if (!dynamicNumber.isEmpty()) {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + Uri.encode(dynamicNumber)));
                    if (dialIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(dialIntent);
                    } else {
                        Toast.makeText(Call.this, "Dialer hardware application context missing.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Call.this, "Please insert target phone number details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupKeypadButton(int viewId, final String appendValue) {
        View targetButton = findViewById(viewId);
        if (targetButton != null) {
            targetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e1.append(appendValue);
                }
            });
        }
    }
}