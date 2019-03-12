package io.github.controlwear.joystickdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketClient socketClient = new SocketClient();// initialize();
        socketClient.execute();

        final JoystickView joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
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

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
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

}