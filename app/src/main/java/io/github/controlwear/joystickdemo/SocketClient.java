package io.github.controlwear.joystickdemo;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;

public class SocketClient  extends AsyncTask {
    private static Socket socket;
    private static String state = "Off";
    private boolean socketClosed = false;

    public static String getState(){
        return state;
    }

    public boolean closeSocket(){

        try {
            if(socket != null)
                socket.close();
            state = "Disconnected";
            socketClosed = true;
            return true;
        }
        catch (IOException io) {
            state = "Failed to close socket";
            return false;
        }
    }


    private void initialize(){
        if(!socketClosed){
            state = "Trying to connect...";
            try{
                // Establish connection
                socket = new Socket(ConfigurationService.getRobotIpAddress(), 9999);

                state = "Connected";
            }
            catch (NoRouteToHostException eNoRoute){
                eNoRoute.printStackTrace();
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                }
                state = "No route to host";
                initialize();
            }
            catch (IOException io) {
                io.printStackTrace();
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                }
                initialize();
            }
        }
    }

    public static void sendMessage(String message){

        try {
            // Request data
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

           // DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            outputStream.write(message.getBytes("UTF-8"), 0, message.length());

     /*       while(!inputStream.readUTF().equals(message)){
                outputStream.write(message.getBytes("UTF-8"), 0, message.length());
            }
            */


            //while()
        }
        catch (IOException io) {
            io.printStackTrace();
            state = "Disconnected";
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        initialize();
        return null;
    }
}
