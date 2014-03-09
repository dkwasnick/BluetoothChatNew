package com.example.bluetoothchat;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final int REQUEST_BT = 999;
	Context context;
	Button create,join;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        create = (Button) findViewById(R.id.createButton);
        join = (Button) findViewById(R.id.joinButton);
        
        
        enableBt();
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
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_BT)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				create.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(context, hostChat.class);
						startActivity(i);
					}
				});
		        
		        join.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(context, joinChat.class);
						startActivity(i);
					}
				});
				
			}
		}
	}
	
    
}
