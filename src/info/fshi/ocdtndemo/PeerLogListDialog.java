package info.fshi.ocdtndemo;

import info.fshi.ocdtndemo.log.PeerLogListAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ListView;

public class PeerLogListDialog extends Dialog {

private PeerLogListAdapter peerLogListAdapter;
	
	private Context mContext;
	
	public PeerLogListDialog(Context context){
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_peer_log);
		mContext = context;
		
		peerLogListAdapter = new PeerLogListAdapter(mContext, R.layout.log);
		
		ListView peerLogLv = (ListView) findViewById(R.id.log_list);
		peerLogLv.setAdapter(peerLogListAdapter);
		
		peerLogListAdapter.notifyDataSetChanged();
	}
	
}
