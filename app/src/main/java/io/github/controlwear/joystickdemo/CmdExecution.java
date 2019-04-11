package io.github.controlwear.joystickdemo;

import android.os.AsyncTask;

public class CmdExecution extends AsyncTask<Integer, Void, String> {

    private static final String LEFT_SIDE_FORWARD = "LF";
    private static final String LEFT_SIDE_BACKWARD = "LB";
    private static final String LEFT_SIDE_STOP = "SL";

    private static final String RIGHT_SIDE_FORWARD = "RF";
    private static final String RIGHT_SIDE_BACKWARD = "RB";
    private static final String RIGHT_SIDE_STOP = "SR";

    private static final String RESET_MESSAGE = "reset";

    private static final int EVENT_TYPE_RESET_ROBOT = 0;
    private static final int EVENT_TYPE_DIRECTION = 1;

    @Override
    protected String doInBackground(Integer... integers) {
        if(!DirectionSystemService.isExecutionLocked()){
            //DirectionSystemService.setExecutionLocked();
            directionEventAsync(integers[0], integers[1], integers[2], integers[3]);
            return "Executed";
        }
        return "Busy";
    }

    private void directionEventAsync(int type, int arg1, int arg2, int arg3){
        // Type 0 = Reset robot
        // Type 1 = DirectionEvent
        this.handleEventType(type, arg1, arg2, arg3);
    }

    private void handleEventType(int type, int arg1, int arg2, int arg3){
        switch (type){
            case EVENT_TYPE_RESET_ROBOT:
                handleResetRobot();
                break;
            case EVENT_TYPE_DIRECTION:
                handleDirectionEvent(arg1, arg2, arg3);
                break;
        }
    }
    private void handleResetRobot() {
        SocketClient.sendMessage(RESET_MESSAGE);
    }

    private void handleDirectionEvent(int y, int pourcent, int isRightSide){
        String direction = Util.getDirectionForYAxis(y);
        int power = Util.getDirectionPower(pourcent);

        switch(isRightSide){
            case 0:
                directionLeftSide(direction, power);
                break;
            case 1:
                directionRightSide(direction, power);
                break;
        }
    }

    private void directionLeftSide(String direction, int power){
        String directionMessage = "";

        switch (direction){
            case "Forward":
                directionMessage = LEFT_SIDE_FORWARD + power;
                break;
            case "Backward":
                directionMessage = LEFT_SIDE_BACKWARD + power;
                break;
            case "None":
                directionMessage = LEFT_SIDE_STOP;
                break;
        }

        if(DirectionSystemService.updateDirectionLeftSide(directionMessage)){
            SocketClient.sendMessage(directionMessage);
        }
    }

    private void directionRightSide(String direction, int power){
        String directionMessage = "";

        switch (direction){
            case "Forward":
                directionMessage = RIGHT_SIDE_FORWARD + power;
                break;
            case "Backward":
                directionMessage = RIGHT_SIDE_BACKWARD + power;
                break;
            case "None":
                directionMessage = RIGHT_SIDE_STOP;
                break;
        }

        if(DirectionSystemService.updateDirectionRightSide(directionMessage)){
            SocketClient.sendMessage(directionMessage);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //DirectionSystemService.setExecutionUnlocked();
    }



    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}