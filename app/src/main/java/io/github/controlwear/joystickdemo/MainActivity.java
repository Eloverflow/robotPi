package io.github.controlwear.joystickdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    TextView connectionStateText;
    JoystickView joystickLeft;
    JoystickView joystickRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketClient socketClient = new SocketClient();// initialize();
        socketClient.execute();


        connectionStateText = (TextView) findViewById(R.id.textViewConnectionState);
        startMessageHandler();

        joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
        joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
                int y = joystickLeft.getNormalizedY();
                Log.d("y", y + "");
                Log.d("strength", strength + "");
                new DirectionSystemOperation().execute(y, strength, 0);
            }
        }, 200);

        joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
                int y = joystickRight.getNormalizedY();
                Log.d("y", y + "");
                Log.d("strength", strength + "");
                new DirectionSystemOperation().execute(y, strength, 1);

            }
        }, 200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketClient.closeSocket();
    }


    private void startMessageHandler() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            String socketState = SocketClient.getState();

                            if(joystickLeft.isEnabled() && socketState.equals("Off") || socketState.equals("Disconnected") || socketState.equals("Trying to connect...")){
                                joystickLeft.setEnabled(false);
                                joystickRight.setEnabled(false);
                            }
                            else if(socketState.equals("Connected")){
                                joystickLeft.setEnabled(true);
                                joystickRight.setEnabled(true);
                            }

                            connectionStateText.setText("Connection state: " + socketState);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

}