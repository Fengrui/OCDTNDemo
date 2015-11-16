package info.fshi.ocdtndemo.http;

import info.fshi.ocdtndemo.packet.BasicPacket;
import info.fshi.ocdtndemo.utils.Constants;
import info.fshi.ocdtndemo.utils.Devices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class WebServerConnector {

	public static final int REGISTRATION_SUCCESS = 1;
	public static final int REGISTRATION_FAIL = 11;
	public static final int UPDATE_SUCCESS = 2;

	private final static String TAG = "web server connector";

	public WebServerConnector(){

	}


	private class SendSensorDataTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... data) {
			// init the counter
			String content = data[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.WEB_SERVER_ADDR + "sensordata");
			try {
				JSONObject json = new JSONObject();
				json.put(BasicPacket.KEY_DATA_CONTENT, content);

				StringEntity params = new StringEntity(json.toString());
				httppost.addHeader("content-type", "application/json");
				httppost.setEntity(params);
				httpclient.execute(httppost);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} finally {
				httppost.abort();
			}

			return null;
		}
	}

	public void reportSensorData(String data) {
		// Create a new HttpClient and Post Header
		new SendSensorDataTask().execute(data);
	}
	
	private class ResourceRegistrationTask extends AsyncTask<ResourceData, Void, Integer> {

		Handler mHandler;
		Messenger mMessenger;

		public ResourceRegistrationTask(Handler handler){
			mHandler = handler;
			mMessenger = new Messenger(mHandler);
		}

		protected Integer doInBackground(ResourceData... data) {
			// init the counter
			ResourceData resourceData = data[0];
			int resourceId = 0;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.WEB_SERVER_ADDR + "register");
			try {
				JSONObject json = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject arrayItem = new JSONObject();
				arrayItem.put(BasicPacket.KEY_ATTRIBUTE_NAME, "address");
				arrayItem.put(BasicPacket.KEY_ATTRIBUTE_TYPE, "string");
				arrayItem.put(BasicPacket.KEY_ATTRIBUTE_VALUE, resourceData.address);
				jsonArray.put(arrayItem);
				json.put(BasicPacket.KEY_RESOURCE_TYPE, resourceData.type);
				json.put(BasicPacket.KEY_RESOURCE_ATTRIBUTES, jsonArray);

				StringEntity params = new StringEntity(json.toString());
				httppost.addHeader("content-type", "application/json");
				httppost.setEntity(params);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				resourceId = Integer.parseInt(responseString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} finally {
				httppost.abort();
			}
			return resourceId;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Message m = new Message();
			if(result > 0){
				m.what = REGISTRATION_SUCCESS;
			}else{
				m.what = REGISTRATION_FAIL;
			}
			m.arg1 = result;
			try {
				mMessenger.send(m);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class updateDeviceList extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// init the counter
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.WEB_SERVER_ADDR + "list");
			try {
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				try {
					JSONObject deviceList = new JSONObject(responseString);
					Devices.PARTICIPATING_DEVICES = new HashMap<String, Integer>();
					Devices.PARTICIPATING_DEVICES_ID = new HashMap<String, Integer>();
					Devices.PARTICIPATING_PHONES = new ArrayList<Integer>();
					Iterator<String> keysItr = deviceList.keys();
					while(keysItr.hasNext()) {
						String key = keysItr.next();
						String value = deviceList.getString(key);
						String[] values = value.split(":");

						Devices.PARTICIPATING_DEVICES.put(key, Integer.parseInt(values[0]));
						Devices.PARTICIPATING_DEVICES_ID.put(key, Integer.parseInt(values[1]));
						if(Integer.parseInt(values[0]) == Devices.DEVICE_TYPE_RELAY){
							Devices.PARTICIPATING_PHONES.add(Integer.parseInt(values[1]));
						}
					}
					Log.d(TAG, Devices.PARTICIPATING_DEVICES_ID.toString());
					Log.d(TAG, Devices.PARTICIPATING_DEVICES.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} finally {
				httppost.abort();
			}
			return null;
		}
	}

	
	private class SendTransactionRecordTask extends AsyncTask<TransactionData, Void, Void> {
		protected Void doInBackground(TransactionData... data) {
			// init the counter
			TransactionData txData = data[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.WEB_SERVER_ADDR + "data");

			try {
				JSONObject json = new JSONObject();
				json.put(BasicPacket.KEY_DATA_SOURCE, txData.senderAddr);
				json.put(BasicPacket.KEY_DATA_DESTINATION, txData.receiverAddr);
				json.put(BasicPacket.KEY_DATA_PACKETID, txData.packetId);
				json.put(BasicPacket.KEY_DATA_PATH, String.valueOf(txData.path));
				json.put(BasicPacket.KEY_DATA_PACKETSIZE, txData.packetSize);

				StringEntity params = new StringEntity(json.toString());
				httppost.addHeader("content-type", "application/json");
				httppost.setEntity(params);

				//HttpResponse response = 
				httpclient.execute(httppost);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} finally {
				httppost.abort();
			}
			return null;
		}
	}

	public void reportTransactionData(TransactionData data) {
		// Create a new HttpClient and Post Header
		new SendTransactionRecordTask().execute(data);
	}

	public void registerResource(ResourceData data, Handler handler) {
		// Create a new HttpClient and Post Header
		new ResourceRegistrationTask(handler).execute(data);
	}

	public void updateDeviceList() {
		// Create a new HttpClient and Post Header
		new updateDeviceList().execute();
	}
}