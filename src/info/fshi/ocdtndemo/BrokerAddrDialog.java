package info.fshi.ocdtndemo;

import info.fshi.ocdtndemo.utils.Constants;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class BrokerAddrDialog extends Dialog {

	public BrokerAddrDialog(Context context){
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_broker_addr);

		Button updateButton = (Button) findViewById(R.id.broker_update_button);
		updateButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				EditText brokerAddr = (EditText) findViewById(R.id.editBrokerAddr);
				Constants.MQTT_BROKER_ADDR = "tcp://" + brokerAddr.getText().toString();
				BrokerAddrDialog.this.dismiss();
			}
		});
	}
}
