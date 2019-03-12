package io.github.controlwear.joystickdemo;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient  extends AsyncTask {
    private static Socket socket;

    private static void initialize(){
        try{
            // Establish connection
            socket = new Socket("192.168.2.210", 10000);
        }
            catch (IOException io) {
            io.printStackTrace();
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
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        initialize();
        return null;
    }
}