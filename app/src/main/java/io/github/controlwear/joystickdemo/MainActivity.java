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


    private TextView mTextViewAngleLeft;
    private TextView mTextViewStrengthLeft;

    private TextView mTextViewAngleRight;
    private TextView mTextViewStrengthRight;
    private TextView mTextViewCoordinateRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //CHEAT
         /*   if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        */
            SocketClient socketClient = new SocketClient();// initialize();
            socketClient.execute();


            mTextViewAngleLeft = (TextView) findViewById(R.id.textView_angle_left);
            mTextViewStrengthLeft = (TextView) findViewById(R.id.textView_strength_left);

            final JoystickView joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
            joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onMove(int angle, int strength) {
                    mTextViewAngleLeft.setText(angle + "°");
                    mTextViewStrengthLeft.setText(strength + "%");

                    int y = joystickLeft.getNormalizedY();
                    Log.d("y", y + "");
                    Log.d("strength", strength + "");
                    new DirectionSystemOperation().execute(y, strength, 0);
                }
            }, 200);

          /*  joystickLeft.setOnTouchListener(new JoystickView.OnTouchListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    if (arg1.getAction()==MotionEvent.ACTION_UP){
                        Log.d("released", "");
                        new DirectionSystemOperation().execute(0, 0, 0);
                    }

                    return true;
                }
            });*/

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

                    int y = joystickRight.getNormalizedY();
                    Log.d("y", y + "");
                    Log.d("strength", strength + "");
                    new DirectionSystemOperation().execute(y, strength, 1);

                }
            }, 200);


        /*    joystickRight.setOnTouchListener(new JoystickView.OnTouchListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    if (!(arg1.getAction()==MotionEvent.ACTION_DOWN)){
                        new DirectionSystemOperation().execute(0, 0, 1);
                    }

                    return true;
                }
            });
*/




    }

}