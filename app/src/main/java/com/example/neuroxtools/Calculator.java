package com.example.neuroxtools;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.Stack;

public class Calculator extends AppCompatActivity implements View.OnClickListener {

    TextView tv1, tv2;
    String currentExpression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        tv1= findViewById(R.id.tvSciExpression);
        tv2 = findViewById(R.id.tvSciResult);

        Button btnSciBack = findViewById(R.id.btnSciBack);
        if (btnSciBack != null) {
            btnSciBack.setOnClickListener(v -> finish());
        }
        int[] buttonIds = {
                R.id.btnSci0, R.id.btnSci1, R.id.btnSci2, R.id.btnSci3, R.id.btnSci4,
                R.id.btnSci5, R.id.btnSci6, R.id.btnSci7, R.id.btnSci8, R.id.btnSci9,
                R.id.btnSciPlus, R.id.btnSciMinus, R.id.btnSciMultiply, R.id.btnSciDivide,
                R.id.btnSciBrackOpen, R.id.btnSciBrackClose, R.id.btnSciDot,
                R.id.btnSciClear, R.id.btnSciBackspace, R.id.btnSciEqual,
                R.id.btnSciSin, R.id.btnSciCos, R.id.btnSciTan, R.id.btnSciLog, R.id.btnSciLn,
                R.id.btnSciPi, R.id.btnSciE, R.id.btnSciPower, R.id.btnSciSqrt
        };

        for (int id : buttonIds) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnSciClear) {
            currentExpression = "";
            tv1.setText("");
            tv2.setText("0");
        } else if (id == R.id.btnSciBackspace) {
            if (currentExpression.endsWith("sin(") || currentExpression.endsWith("cos(") || currentExpression.endsWith("tan(")) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 4);
            } else if (currentExpression.endsWith("log(") || currentExpression.endsWith("sqrt(")) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 5);
            } else if (currentExpression.endsWith("ln(")) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 3);
            } else if (currentExpression.length() > 0) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            }
            tv1.setText(currentExpression);
        } else if (id == R.id.btnSciEqual) {
            if (!currentExpression.isEmpty()) {
                try {
                    double calculationResult = new ScientificExpressionEvaluator().evaluate(currentExpression);
                    if (Double.isNaN(calculationResult) || Double.isInfinite(calculationResult)) {
                        tv2.setText("Error");
                    } else if (calculationResult == (long) calculationResult) {
                        tv2.setText(String.valueOf((long) calculationResult));
                    } else {
                        tv2.setText(String.valueOf(calculationResult));
                    }
                } catch (Exception e) {
                    tv2.setText("Error");
                }
            }
        } else {
            MaterialButton clickedButton = (MaterialButton) v;
            String buttonText = clickedButton.getText().toString();
            if (buttonText.equals("sin") || buttonText.equals("cos") || buttonText.equals("tan") ||
                    buttonText.equals("log") || buttonText.equals("ln")) {
                currentExpression += buttonText + "(";
            } else if (buttonText.equals("√")) {
                currentExpression += "sqrt(";
            } else if (buttonText.equals("π")) {
                currentExpression += "3.14159265359";
            } else if (buttonText.equals("e")) {
                currentExpression += "2.71828182846";
            } else {
                currentExpression += buttonText;
            }
            tv1.setText(currentExpression);
        }
    }
    private static class ScientificExpressionEvaluator {
        public double evaluate(final String str) {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < str.length()) throw new RuntimeException("Unexpected character context code: " + (char)ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if      (eat('*')) x *= parseFactor();
                        else if (eat('/')) x /= parseFactor();
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return +parseFactor();
                    if (eat('-')) return -parseFactor();

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z') {
                        while (ch >= 'a' && ch <= 'z') nextChar();
                        String func = str.substring(startPos, this.pos);
                        if (eat('(')) {
                            x = parseExpression();
                            eat(')');
                        } else {
                            x = parseFactor();
                        }
                        switch (func) {
                            case "sqrt": x = Math.sqrt(x); break;
                            case "sin":  x = Math.sin(Math.toRadians(x)); break;
                            case "cos":  x = Math.cos(Math.toRadians(x)); break;
                            case "tan":  x = Math.tan(Math.toRadians(x)); break;
                            case "log":  x = Math.log10(x); break;
                            case "ln":   x = Math.log(x); break;
                            default: throw new RuntimeException("Unknown expression operator block string structural tag: " + func);
                        }
                    } else {
                        throw new RuntimeException("Unexpected character index match trace: " + (char)ch);
                    }

                    if (eat('^')) x = Math.pow(x, parseFactor());
                    return x;
                }
            }.parse();
        }
    }
}