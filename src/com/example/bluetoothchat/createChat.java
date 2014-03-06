package com.example.bluetoothchat;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createChat extends Activity {
	
	int REQUEST_BT = 999;
	int REQUEST_DISCOVER = 1000;
	Context context;
	Button create;
	EditText username;
	EditText roomName;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        
        context = this;
        create = (Button) findViewById(R.id.button1);
        username = (EditText) findViewById(R.id.textView1);
        roomName = (EditText) findViewById(R.id.textView2);
        
        create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (username.getText().toString().length() > 0)
				{
					enableBt();
				}
				
			}
		});
        
	}

	private void enableBt()
	{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			return;
		}
		
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_BT);
		}else{
			goDiscoverable();
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_BT)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				
				goDiscoverable();	
			}
		}else if (requestCode == REQUEST_DISCOVER)
		{
			if (resultCode != Activity.RESULT_CANCELED)
			{
				Intent i = new Intent(context, hostChat.class);
				i.putExtra("un", roomName.getText().toString());
				startActivity(i);
			}
		}
	}
	
	private void goDiscoverable()
	{
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		startActivityForResult(discoverableIntent, REQUEST_DISCOVER);
	}
	
	
	
	
	

	
}


