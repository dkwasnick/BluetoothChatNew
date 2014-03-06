package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class joinChat extends Activity {
	
	Context context;
	ConnectThread connectThread;
	ListView listView;
	ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	ArrayList<String> devicesNames = new ArrayList<String>();
	
	
	TextView mainChat;
	Button button;
	EditText input;
	
	ConnectedThread connectedThread;
	BluetoothSocket serverSocket;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        context = this;
       
        
        
        listView = (ListView) findViewById(R.id.listView1);
       
        getPairedDevices();

        
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
	        
	        System.out.println("DEVICE NAME = "+device.getName());
	        
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("f820b940-a4ef-11e3-a5e2-0800200c9a66"));
	        } catch (IOException e) { 
	        	System.out.println("Could not create rf comm socket connection");
	        	e.printStackTrace();
	        }
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
	        	connectException.printStackTrace();
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
		
		serverSocket = socket;
		System.out.println("socket connected and managed woo");
		
		setContentView(R.layout.activity_chat);
		mainChat = (TextView) findViewById(R.id.textView1);
		button = (Button) findViewById(R.id.button1);
		input = (EditText) findViewById(R.id.editText1);
		
		
		
		
		connectedThread = new ConnectedThread(serverSocket);
		connectedThread.start();
		
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				connectedThread.write(input.getText().toString().getBytes());		
			}
		});
		
	}
	
	
private class ConnectedThread extends Thread {
	    
		private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	    Handler mHandler = new Handler(Looper.getMainLooper());
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { 
	        	e.printStackTrace();
	        }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                // Send the obtained bytes to the UI activity
	                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
	                
	                mainChat.setText(mainChat.getText().toString()+"\n"+buffer);
	                
	                
	            } catch (IOException e) {
	            	e.printStackTrace();
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	    }
	}
	
}
