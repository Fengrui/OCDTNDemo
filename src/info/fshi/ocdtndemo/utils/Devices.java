package info.fshi.ocdtndemo.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Devices {

	public static int DEVICE_TYPE_SENSOR = 1;
	public static int DEVICE_TYPE_RELAY = 2;
	public static int DEVICE_TYPE_SINK = 3;
	
	public static int MY_TYPE = DEVICE_TYPE_RELAY;
	
	public static HashMap<String, Integer> PARTICIPATING_DEVICES = new HashMap<String, Integer>();
    
    public static HashMap<String, Integer> PARTICIPATING_DEVICES_ID = new HashMap<String, Integer>();
    
    public static ArrayList<Integer> PARTICIPATING_PHONES = new ArrayList<Integer>();
}
