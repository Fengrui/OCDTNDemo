package info.fshi.ocdtndemo;

import info.fshi.ocdtndemo.http.ResourceData;
import info.fshi.ocdtndemo.http.WebServerConnector;
import info.fshi.ocdtndemo.utils.Devices;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	
	private Context mContext;
	WebServerConnector mWebConnector;
	private BluetoothAdapter mBluetoothAdapter = null;
	private final int REQUEST_BT_ENABLE = 1;
	private final int REQUEST_BT_DISCOVERABLE = 11;
	private int RESULT_BT_DISCOVERABLE_DURATION = 0;
	
	private final static String TAG = "registrationactivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		mContext = this;
		mWebConnector = new WebServerConnector();
		// credit UI activity
		// it shows the current credit the user has received, also notifies any credit change
		// it starts the scanning alarm

		initBluetoothUtils();
		
		final RegistrationHandler handler = new RegistrationHandler();
		
		Button registrationButtion = (Button) findViewById(R.id.registration_buttion);
		registrationButtion.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ResourceData data = new ResourceData();
				if(mBluetoothAdapter.getAddress().equalsIgnoreCase("BC:EE:7B:B0:7E:5A")){
					data.type = Devices.DEVICE_TYPE_SINK;
				}else{
					data.type = Devices.DEVICE_TYPE_RELAY;
				}
				data.address = mBluetoothAdapter.getAddress();
				mWebConnector.registerResource(data, handler);
			}
		});
		
		Button applicationButtion = (Button) findViewById(R.id.start_application_button);
		applicationButtion.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, CreditActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@SuppressLint("HandlerLeak") private class RegistrationHandler extends Handler {


		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case WebServerConnector.REGISTRATION_SUCCESS:
				Button registrationButtion = (Button) findViewById(R.id.registration_buttion);
				registrationButtion.setBackgroundResource(R.drawable.connected_button);
				registrationButtion.setText(R.string.action_registered);
				Button applicationButton = (Button) findViewById(R.id.start_application_button);
				applicationButton.setEnabled(true);
				applicationButton.setBackgroundResource(R.drawable.connect_button);
				Log.d(TAG, "my id is " + String.valueOf(msg.arg1));
				mWebConnector.updateDeviceList();
				break;
			case WebServerConnector.REGISTRATION_FAIL:
				break;
			default:
				break;
			}
		}
	}
	
	private void initBluetoothUtils(){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(mContext, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		switch (requestCode){
		case REQUEST_BT_ENABLE:
			if (resultCode == RESULT_OK) {
				// start bluetooth utils
				initBluetoothUtils();
				Intent discoverableIntent = new
						Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, RESULT_BT_DISCOVERABLE_DURATION);
				startActivityForResult(discoverableIntent, REQUEST_BT_DISCOVERABLE);
			}
			else{
				Log.d(TAG, "Bluetooth is not enabled by the user.");
			}
			break;
		case REQUEST_BT_DISCOVERABLE:
			if (resultCode == RESULT_CANCELED){
				Log.d(TAG, "Bluetooth is not discoverable.");
			}
			else{
				Log.d(TAG, "Bluetooth is discoverable by 300 seconds.");
			}
			break;
		default:
			break;
		}
	}
	
}
