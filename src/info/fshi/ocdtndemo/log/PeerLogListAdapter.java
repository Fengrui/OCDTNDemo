package info.fshi.ocdtndemo.log;

import info.fshi.ocdtndemo.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PeerLogListAdapter extends ArrayAdapter<PeerLog> {
	Context context;
	int layoutResourceId;

	public PeerLogListAdapter(Context context, int resourceid) {
		super(context, resourceid, PeerLogList.peerLogList);
		this.layoutResourceId = resourceid;
		this.context = context;
	}
	
	/**
	 * comparator to sort list
	 * @author fshi
	 */
	public class PeerLogComparator implements Comparator<PeerLog>
	{
		@Override
		public int compare(PeerLog lhs, PeerLog rhs) {
			// TODO Auto-generated method stub
			return (int) (lhs.sTimestamp - rhs.sTimestamp);
		}
	}


	public void sortList(){
		Collections.sort(PeerLogList.peerLogList, new PeerLogComparator());
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PeerLogHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new PeerLogHolder();

			holder.timestamp = (TextView)row.findViewById(R.id.peer_timestamp);
			holder.dir = (TextView)row.findViewById(R.id.peer_direction);
			holder.mac = (TextView)row.findViewById(R.id.peer_mac_addr);
			holder.len = (TextView)row.findViewById(R.id.peer_queue_len);
//			holder.ctime = (TextView)row.findViewById(R.id.peer_connect_time);
			row.setTag(holder);
		}
		else
		{
			holder = (PeerLogHolder)row.getTag();
		}

		PeerLog peerLog = PeerLogList.peerLogList.get(position);
		if(peerLog != null){
			holder.timestamp.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CHINA).format(new java.util.Date(peerLog.sTimestamp)));
			if(peerLog.dir == 0){
				holder.dir.setText("snd");
			}else{
				holder.dir.setText("rec");
			}
			
			holder.mac.setText(String.valueOf(peerLog.mac));
			holder.len.setText(String.valueOf(peerLog.qLen));
//			holder.ctime.setText(String.valueOf(peerLog.eTimestamp - peerLog.sTimestamp) + "ms");
		}
		return row;
	}

	static class PeerLogHolder
	{
		TextView timestamp;
		TextView dir;
		TextView mac;
		TextView len;
		TextView ctime;
	}
}
