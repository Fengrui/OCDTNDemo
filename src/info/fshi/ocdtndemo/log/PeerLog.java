package info.fshi.ocdtndemo.log;

public class PeerLog {
	public int dir; // 0=send, 1=receive
	public String mac;
	public int qLen;
	public long sTimestamp;
	public long eTimestamp;
}
