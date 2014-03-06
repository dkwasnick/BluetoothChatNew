package com.example.bluetoothchat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class joinChat extends Activity {
	
	Context context;
	EditText username, roomName;
	Button join;
	ConnectThread connectThread;
	ListView listView;
	ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	ArrayList<String> devicesNames = new ArrayList<String>();
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        context = this;
        username = (EditText) findViewById(R.id.editText1);
        roomName = (EditText) findViewById(R.id.editText2);
       
        
        join = (Button) findViewById(R.id.button1);
        
        listView = (ListView) findViewById(R.id.listView1);
       
        getPairedDevices();

        
        join.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connectThread = new ConnectThread(null);
				connectThread.run();
			}
		});
	}
	
	
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(UUID.nameUUIDFromBytes(roomName.getText().toString().getBytes()));
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        manageConnectedSocket(mmSocket);
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	
	
	private void getPairedDevices()
	{
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		

		if (pairedDevices.size() > 0) {
			
		    // Loop through paired devices
			ArrayList<String> names = new ArrayList<String>();
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	names.add(device.getName()+"\n"+device.getAddress());
		    }
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, names );
            listView.setAdapter(adapter);
            
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            		{

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
						{
							BluetoothDevice dev = (BluetoothDevice) pairedDevices.toArray()[arg2];
							connectTo(dev);
							
						}
            	
            		});
		    
		}
	}
	
	private void connectTo(BluetoothDevice device)
	{
		ConnectThread ct = new ConnectThread(device);
		ct.run();
	}
	
	private void manageConnectedSocket(BluetoothSocket socket)
	{
		System.out.println("socket connected and managed woo");
	}
	
}
