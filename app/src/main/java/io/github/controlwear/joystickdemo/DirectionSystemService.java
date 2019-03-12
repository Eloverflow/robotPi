package io.github.controlwear.joystickdemo;

public class DirectionSystemService {
    private static String directionLeftSide;
    private static String directionRightSide;
    private static boolean executionLocked;

    public static boolean updateDirectionLeftSide(String directionMessage){
        if(getDirectionLeftSide() == null || !getDirectionLeftSide().equals(directionMessage)){
            setDirectionLeftSide(directionMessage);
            return true;
        }
        return false;
    }

    public static boolean updateDirectionRightSide(String directionMessage){
        if(getDirectionRightSide() == null || !getDirectionRightSide().equals(directionMessage)){
            setDirectionRightSide(directionMessage);
            return true;
        }
        return false;
    }

    public static String getDirectionLeftSide(){
        return directionLeftSide;
    }

    public static String getDirectionRightSide(){
        return directionRightSide;
    }

    private static void setDirectionLeftSide(String newDirection){
        directionLeftSide = newDirection;
    }

    private static void setDirectionRightSide(String newDirection){
        directionRightSide = newDirection;
    }

    public static boolean isExecutionLocked(){
        return executionLocked;
    }

    public static void setExecutionLocked(){
        executionLocked = true;
    }

    public static void setExecutionUnlocked(){
        executionLocked = false;
    }
}
