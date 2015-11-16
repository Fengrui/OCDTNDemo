package info.fshi.ocdtndemo.data;

import info.fshi.ocdtndemo.utils.Constants;
import info.fshi.ocdtndemo.utils.Devices;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class QueueManager {

	private final static String TAG = "QueueManager";

	public int ID;

	private ArrayList<QueueItem> queue = new ArrayList<QueueItem>();
	
	public long sensorTimestamp = 0;
	public long sinkTimestamp = 0;

	public int packetsReceived = 0;
	public int packetsSent = 0;
	
	// queue is a json with format {sequence of ids : content}

	public QueueManager(String s){
		// for test purpose, init queue content
		if(Devices.PARTICIPATING_DEVICES_ID.get(s) != null){
			ID = Devices.PARTICIPATING_DEVICES_ID.get(s);
		}
		if(Constants.DEBUG){
			initQueue();
			Log.d(TAG, "init queue");
		}
	}

	private void initQueue(){
		Random rand = new Random();
		for(int i=0; i<rand.nextInt(20) + 5; i++){
			StringBuffer sb = new StringBuffer();
			sb.append("test");
			QueueItem qItem = new QueueItem();
			qItem.packetId = String.valueOf(new Random().nextInt(1000000));
			qItem.path.add(ID);
			qItem.data = sb.toString();
			queue.add(qItem);
		}
	}

	public QueueItem getFromQueue(String packetId){
		for(QueueItem qItem:queue){
			if(qItem.packetId.equalsIgnoreCase(packetId)){
				return qItem;
			}
		}
		return null;
	}
	
	public void appendToQueue(String packetId, String path, String data){

		QueueItem qItem = new QueueItem();
		qItem.packetId = packetId;
		qItem.data = data;
		String[] IDs = path.split(",");
		boolean hasLoop = false;
		for(String ID:IDs){
			if(Integer.parseInt(ID) == this.ID){
				hasLoop = true;
				break;
			}
			qItem.path.add(Integer.parseInt(ID));
		}
		qItem.path.add(this.ID);
		if(!hasLoop){
			queue.add(qItem);
		}	

	}

	// format: ID1,ID2,ID3 && data

	public String[] getFromQueue(int length, String MAC){
		int peerID = Devices.PARTICIPATING_DEVICES_ID.get(MAC);
		String[] data = new String[3];
		ArrayList<QueueItem> newQueue = new ArrayList<QueueItem>();
		for(QueueItem qItem: queue){
			if(length > 0){
				StringBuffer sb = new StringBuffer();
				boolean hasLoop = false;
				for(int i=0; i< qItem.path.size(); i++){
					if(peerID == qItem.path.get(i)){ // loop detection
						hasLoop = true;
					}
					sb.append(qItem.path.get(i));
					if(i != qItem.path.size() - 1){
						sb.append(",");
					}
				}
				if(hasLoop){
					newQueue.add(qItem);
				}
				else{
					data[0] = sb.toString();
					data[1] = qItem.data;
					data[2] = qItem.packetId;
					length -= 1;
				}
			}else{
				newQueue.add(qItem);
			}
		}
		queue = newQueue;
		return data;
	}

	public String[] getFromQueue(){ // get the first one from the queue
		String[] data = new String[3];
		if(queue.size() > 0){
			StringBuffer sb = new StringBuffer();

			QueueItem qItem = queue.get(0);
			
			for(int i=0; i< qItem.path.size(); i++){
				sb.append(qItem.path.get(i));
				if(i != qItem.path.size() - 1){
					sb.append(",");
				}
			}

			data[0] = sb.toString();
			data[1] = qItem.data;
			data[2] = qItem.packetId;
			queue.remove(0);
		}
		return data;
	}

	/*
	 public JSONArray getFromQueue(){

		JSONArray buffer = new JSONArray();

		for(QueueItem qItem: queue){
			StringBuffer sb = new StringBuffer();
			for(int i=0; i< qItem.path.size(); i++){
				sb.append(qItem.path.get(i));
				if(i == qItem.path.size() - 1){
					sb.append(":");
				}else{
					sb.append(",");
				}
			}
			sb.append(qItem.data);
			buffer.put(sb.toString());
		}
		queue = new ArrayList<QueueItem>();
		return buffer;
	}*/

	public int getQueueLength(){
		Log.d(TAG, "queue len is " + String.valueOf(queue.size()));
		for(QueueItem qItem: queue){
			Log.d(TAG, qItem.packetId + "@" + qItem.path.toString() + ":" + qItem.data);
		}
		return queue.size();
	}
}
