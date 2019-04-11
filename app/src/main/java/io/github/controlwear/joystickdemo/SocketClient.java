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

    public static String getState(){
        return state;
    }

    public static boolean closeSocket(){

        try {
            socket.close();
            state = "Disconnected";
            return true;
        }
        catch (IOException io) {
            state = "Failed to close socket";
            return false;
        }
    }

    private static void initialize(){
        state = "Trying to connect...";
        try{
            // Establish connection
            socket = new Socket("192.168.2.215", 9999);

            state = "Connected";
        }
        catch (NoRouteToHostException eNoRoute){
            eNoRoute.printStackTrace();
            state = "No route to host";
            initialize();
        }
        catch (IOException io) {
            io.printStackTrace();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                state = "Interrupted";
            }
            initialize();
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
            initialize();
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        initialize();
        return null;
    }
}
