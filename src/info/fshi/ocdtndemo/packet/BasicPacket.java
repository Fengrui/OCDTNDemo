package info.fshi.ocdtndemo.packet;

public abstract class BasicPacket {
	// BT messge header
	// format:  type|data
	public static final String PACKET_TYPE = "type";
	public static final String PACKET_ID = "id";
	public static final String PACKET_PATH = "path";
	public static final String PACKET_DATA = "data";
	
	// data type identifier
	public static final int PACKET_TYPE_QUEUE_SIZE = 100;
	public static final int PACKET_TYPE_QUEUE_SIZE_REQUEST = 101;
	public static final int PACKET_TYPE_DATA = 102;
	public static final int PACKET_TYPE_DATA_ACK = 103;
	
	public final static String KEY_RESOURCE_TYPE = "type";
	public final static String KEY_RESOURCE_ATTRIBUTES = "attributes";
	public final static String KEY_ATTRIBUTE_NAME = "name";
	public final static String KEY_ATTRIBUTE_TYPE = "type";
	public final static String KEY_ATTRIBUTE_VALUE = "value";
	
	public final static String KEY_DATA_CONTENT = "content";
	public final static String KEY_DATA_SOURCE = "source";
	public final static String KEY_DATA_DESTINATION = "destination";
	public final static String KEY_DATA_PACKETID = "packetid";
	public final static String KEY_DATA_PATH = "path";
	public final static String KEY_DATA_PACKETSIZE = "packetsize";
	
}
