package io.github.controlwear.joystickdemo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    SocketClient socketClient;
    TextView connectionStateText;
    JoystickView joystickLeft;
    JoystickView joystickRight;
    Button resetSwitch;
    Button changeIpBtn;
    EditText txtUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUrl = new EditText(this);

        socketClient = new SocketClient();// initialize();
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
                new CmdExecution().execute(1, y, strength, 0);
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
                new CmdExecution().execute(1, y, strength, 1);

            }
        }, 200);


        resetSwitch = (Button) findViewById(R.id.reset_switch);
        resetSwitch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CmdExecution().execute(0, 0, 0, 0);
            }
        });




        changeIpBtn = (Button) findViewById(R.id.change_ip_btn);
        changeIpBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the default text to a link of the Queen
                txtUrl.setHint(ConfigurationService.getRobotIpAddress());
                displayIpModal();
            }
        });
    }

    public void displayIpModal(){
        if(txtUrl.getParent() != null) {
            ((ViewGroup)txtUrl.getParent()).removeView(txtUrl); // <- fix
        }

        AlertDialog.Builder ipAddressDialog;

        ipAddressDialog = new AlertDialog.Builder(this)
                .setTitle("IP Address")
                .setMessage("Please specify robot ip address")
                .setView(txtUrl)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String ipAddress = txtUrl.getText().toString();
                        ConfigurationService.setRobotIpAddress(ipAddress);

                        socketClient.closeSocket();
                        socketClient = new SocketClient();// initialize();
                        socketClient.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ipAddressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socketClient.closeSocket();
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