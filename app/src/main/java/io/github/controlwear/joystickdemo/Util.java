package io.github.controlwear.joystickdemo;

public class Util {
    public static String getDirectionForYAxis(int y){
        String direction;
        if(y < 40){
            direction = "Forward";
        } else if (y > 60){
            direction = "Backward";
        } else {
            direction = "None";
        }
        return direction;
    }

    public static int getDirectionPower(int pourcent){
        int power;
        if(pourcent >= 100){
            power = 9;
        } else if(pourcent >= 95){
            power = 7;
        } else if(pourcent >= 90){
            power = 6;
        } else if(pourcent >= 85){
            power = 5;
        } else if(pourcent >= 80){
            power = 4;
        } else if(pourcent >= 65){
            power = 3;
        } else if(pourcent >= 45){
            power = 2;
        } else {
            power = 1;
        }
        return power;
    }
}
