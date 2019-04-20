package io.github.controlwear.joystickdemo;

public class ConfigurationService {
    static final String DEFAULT_IP_ADDRESS = "192.168.2.215";

    private static String robotIpAddress;


    public static String getRobotIpAddress(){
        return robotIpAddress != null ? robotIpAddress : DEFAULT_IP_ADDRESS;
    }

    public static void setRobotIpAddress(String newIpAddress){
        robotIpAddress = newIpAddress;
    }

}
