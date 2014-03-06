package com.example.bluetoothchat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

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

public class hostChat extends Activity {
	
	AcceptThread acceptor = new AcceptThread();
	ArrayList<BluetoothServerSocket> sockets = new ArrayList<BluetoothServerSocket>();
	Context context;
	String username;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        
        
        username = getIntent().getExtras().getString("un");
        context = this;
        
        acceptor.run();
        
        
        
	}

	private class AcceptThread extends Thread {
	    private final BluetoothServerSocket mmServerSocket;
	    private BluetoothAdapter mBluetoothAdapter;
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth Chat", UUID.nameUUIDFromBytes(username.getBytes()));
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                //break;
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

	
}


