package io.github.controlwear.joystickdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {


    private TextView mTextViewAngleLeft;
    private TextView mTextViewStrengthLeft;

    private TextView mTextViewAngleRight;
    private TextView mTextViewStrengthRight;
    private TextView mTextViewCoordinateRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //CHEAT
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }


            // Establish connection
            final Socket socket = new Socket("192.168.2.210", 10000);


            mTextViewAngleLeft = (TextView) findViewById(R.id.textView_angle_left);
            mTextViewStrengthLeft = (TextView) findViewById(R.id.textView_strength_left);

            final JoystickView joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
            joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onMove(int angle, int strength) {
                    mTextViewAngleLeft.setText(angle + "°");
                    mTextViewStrengthLeft.setText(strength + "%");
                    try {
                        // Request data
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                        int y = joystickLeft.getNormalizedY();
                        if(0 <= y && y <= 10){
                            outputStream.writeChars("L9");
                        } else if(10 < y && y <= 20){
                            outputStream.writeChars("L7");
                        } else if(20 < y && y <= 30){
                            outputStream.writeChars("L5");
                        } else if(30 < y && y <= 40){
                            outputStream.writeChars("L3");
                        } else if(40 < y && y < 50){
                            outputStream.writeChars("L1");
                        } else if(y == 50){
                            outputStream.writeChars("SL");
                        } else if(50 < y && y <= 60){
                            outputStream.writeChars("A1");
                        } else if(60 < y && y <= 70){
                            outputStream.writeChars("A3");
                        } else if(70 < y && y <= 80){
                            outputStream.writeChars("A5");
                        } else if(80 < y && y <= 90){
                            outputStream.writeChars("A7");
                        } else if(90 < y && y <= 100){
                            outputStream.writeChars("A9");
                        }

                    }
                    catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            });


            mTextViewAngleRight = (TextView) findViewById(R.id.textView_angle_right);
            mTextViewStrengthRight = (TextView) findViewById(R.id.textView_strength_right);
            mTextViewCoordinateRight = findViewById(R.id.textView_coordinate_right);

            final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
            joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onMove(int angle, int strength) {
                    mTextViewAngleRight.setText(angle + "°");
                    mTextViewStrengthRight.setText(strength + "%");
                    /*mTextViewCoordinateRight.setText(
                            String.format("x%03d:y%03d",
                                    joystickRight.getNormalizedX(),
                                    joystickRight.getNormalizedY())
                    );*/

                    try {
                        // Request data
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                        int y = joystickRight.getNormalizedY();
                        if(0 <= y && y <= 10){
                            outputStream.writeChars("R9");
                        } else if(10 < y && y <= 20){
                            outputStream.writeChars("R7");
                        } else if(20 < y && y <= 30){
                            outputStream.writeChars("R5");
                        } else if(30 < y && y <= 40){
                            outputStream.writeChars("R3");
                        } else if(40 < y && y < 50){
                            outputStream.writeChars("R1");
                        } else if(y == 50){
                            outputStream.writeChars("SR");
                        } else if(50 < y && y <= 60){
                            outputStream.writeChars("P1");
                        } else if(60 < y && y <= 70){
                            outputStream.writeChars("P3");
                        } else if(70 < y && y <= 80){
                            outputStream.writeChars("P5");
                        } else if(80 < y && y <= 90){
                            outputStream.writeChars("P7");
                        } else if(90 < y && y <= 100){
                            outputStream.writeChars("P9");
                        }

                    }
                    catch (IOException io) {
                        io.printStackTrace();
                    }

                }
            });


        }
        catch (IOException io) {
            io.printStackTrace();
        }



    }

}