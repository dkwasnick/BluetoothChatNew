package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class hostChat extends Activity {
	
	private static final int MESSAGE_READ = 999;
	AcceptThread acceptor;
	ArrayList<BluetoothSocket> sockets = new ArrayList<BluetoothSocket>();
	Context context;
	String username;
	TextView mainChat;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        
        username = getIntent().getExtras().getString("un");
        context = this;
        mainChat = (TextView) findViewById(R.id.textView1);
        
        acceptor = new AcceptThread();
        acceptor.start();
        
        
        
	}

	private class AcceptThread extends Thread {
		
		
		
	    private final BluetoothServerSocket mmServerSocket;
	    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    public AcceptThread() {
	    	System.out.println("AcceptThread called");

	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth Chat", UUID.fromString("f820b940-a4ef-11e3-a5e2-0800200c9a66"));
	        } catch (IOException e) { 
	        	e.printStackTrace();
	        }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	    	System.out.println("running...");
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	                System.out.println("accepted connection!");
	            } catch (IOException e) {
	            	System.out.println("socket could not accept");
	            	e.printStackTrace();
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                manageConnectedSocket(socket);
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private void manageConnectedSocket(BluetoothSocket socket)
	{
		System.out.println("accepted socket! woo");
		sockets.add(socket);
		
		
	}
	
	private class ListenSocketThread extends Thread {
	    private BluetoothSocket socket;
	    public ListenSocketThread(BluetoothSocket s) {
	        socket = s;
	    }
	 
	    public void run() {
	        while (true)
	        {
	        	
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            socket.close();
	        } catch (IOException e) { }
	    }
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


