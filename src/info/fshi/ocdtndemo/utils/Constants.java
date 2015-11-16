package info.fshi.ocdtndemo.utils;

public abstract class Constants {

	public static long SCAN_INTERVAL = 20000;
	public static long SCAN_DURATION = 10000;

	public static boolean DEBUG = false;

	public static String TAG_ACT_TEST = "test";

	public static long BT_CLIENT_TIMEOUT = 5000;

	public static long SENSOR_CONTACT_INTERVAL = 60000;
	public static long SINK_CONTACT_INTERVAL = 240000;

	public static int MAX_RELAY_NUM = 2;

	public static String MQTT_BROKER_ADDR = "tcp://192.168.0.21:1883";
	
	public static final String WEB_SERVER_ADDR = "http://192.168.0.10:8000/";

}
