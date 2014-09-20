package com.uve.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {
	TextView out;
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	boolean read = false;
	private Button button1;
	private Button button2;
	private Button button3;
	// Well known SPP UUID
	boolean flashl = false;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// Insert your server's MAC address
	private static String address = "00:00:00:00:00:00";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		out = (TextView) findViewById(R.id.out);

	

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
		button1.setEnabled(false);
		button2.setEnabled(false);
		button3.setEnabled(false);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBTState();
	}

	@Override
	public void onStart() {
		super.onStart();
		out.append("\n...In onStart()...");
	}

	@Override
	public void onResume() {
		super.onResume();

		out.append("\nAttempting connecting client...");

		button1.setEnabled(false);
		button2.setEnabled(false);

		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				if (deviceBTName.toLowerCase().contains("uve")) {
					address = device.getAddress();
					break;
				}
			}
		}

		// Set up a pointer to the remote node using it's address.
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		// Two things are needed to make a connection:
		// A MAC address, which we got above.
		// A Service ID or UUID. In this case we are using the
		// UUID for SPP.
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			out.append("In onResume() and socket create failed: "
					+ e.getMessage() + ".");
		}

		// Discovery is resource intensive. Make sure it isn't going on
		// when you attempt to connect and pass your message.
		btAdapter.cancelDiscovery();

		// Establish the connection. This will block until it connects.
		try {
			btSocket.connect();

			out.append("\nData link opened...\n");
		} catch (IOException e) {
			try {
				btSocket.close();
			} catch (IOException e2) {
				out.append("In onResume() and unable to close socket during connection failure"
						+ e2.getMessage() + ".");
			}
		}



		try {
			outStream = btSocket.getOutputStream();
			inStream = btSocket.getInputStream();
			

			
			read = true;

			TimerTask tt = new TimerTask() {

				@Override
				public void run() {
					while (read) {
						try {
							final int readed = inStream.read();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									out.append("<- got: " + readed + "\n");

								}
							});

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							read = false;
						}

					}

				}
			};


			
			byte[] msgBuffer = new byte[1];
			msgBuffer[0] = 0;

			try {
				outStream.write(msgBuffer);
				for (byte b : msgBuffer)
					out.append("(id) sent: " + b + "\n");
				out.append("id="+inStream.read()+ "\n");
			} catch (IOException e) {
				out.append("Fatal Error " + e.getMessage());
			}
			
			msgBuffer = new byte[1];
			msgBuffer[0] = 9;

			try {
				outStream.write(msgBuffer);
				for (byte b : msgBuffer)
					out.append("(btry) sent: " + b + "\n");
				out.append("btry1="+inStream.read()+ "\n");
				out.append("btry2="+inStream.read()+ "\n");
			} catch (IOException e) {
				out.append("Fatal Error " + e.getMessage());
			}

			Timer t = new Timer();
			t.schedule(tt, 0);
			
			button1.setEnabled(true);
			button2.setEnabled(true);
			button3.setEnabled(true);
		} catch (IOException e) {
			out.append(

			"In onResume() and output stream creation failed:" + e.getMessage()
					+ ".");
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		read = false;
		

		if (outStream != null) {
			try {
				outStream.flush();
			} catch (IOException e) {
				out.append(

				"In onPause() and failed to flush output stream: "
						+ e.getMessage() + ".");
			}
		}

		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				out.append(

				"In onPause() and failed to close input stream: "
						+ e.getMessage() + ".");
			}
		}

		try {
			btSocket.close();
		} catch (IOException e2) {
			out.append("In onPause() and failed to close socket."
					+ e2.getMessage() + ".");
		}
	}

	
	private void CheckBTState() {
		// Check for Bluetooth support and then check to make sure it is turned
		// on

		// Emulator doesn't support Bluetooth and will return null
		if (btAdapter == null) {
			out.append("Bluetooth Not supported. Aborting.\n");

		} else {
			if (btAdapter.isEnabled()) {
				out.append("\nBluetooth ok.");
			} else {
				// Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(
						btAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		byte[] msgBuffer;
		switch (arg0.getId()) {
		case R.id.button1:

			msgBuffer = new byte[1];
			msgBuffer[0] = 1;

			try {
				outStream.write(msgBuffer);
				for (byte b : msgBuffer)
					out.append("\n-> sent: " + b + "\n");

			} catch (IOException e) {
				out.append("Fatal Error " + e.getMessage());
			}
			break;
		case R.id.button2:
			msgBuffer = new byte[5];
			msgBuffer[0] = 32;
			msgBuffer[1] = 120;
			msgBuffer[2] = 120;
			msgBuffer[3] = 1;
			msgBuffer[4] = 10;

			try {
				/*outStream.write(32);//.write(msgBuffer);
				outStream.write(120);
				outStream.write(120);
				outStream.write(1);
				outStream.write(10);*/
				outStream.write(msgBuffer);
				for (byte b : msgBuffer)
					out.append("\n-> sent: " + b + "\n");
			} catch (IOException e) {

				out.append("Fatal Error " + e.getMessage());
			}
			break;

		case R.id.button3:
			if (!flashl) {
				msgBuffer = new byte[2];
				msgBuffer[0] = 35;
				msgBuffer[1] = 0;

				try {
					outStream.write(msgBuffer);
					flashl = true;
					for (byte b : msgBuffer)
						out.append("\n-> sent: " + b + "\n");
				} catch (IOException e) {

					out.append("Fatal Error " + e.getMessage());
				}
			} else {
				msgBuffer = new byte[2];
				msgBuffer[0] = 35;
				msgBuffer[1] = 1;

				try {
					outStream.write(msgBuffer);
					flashl = false;
					for (byte b : msgBuffer)
						out.append("\n-> sent: " + b + "\n");
				} catch (IOException e) {

					out.append("Fatal Error " + e.getMessage());
				}
			}
			out.append("flashlight: " + flashl + "\n");
			break;
		}

	}
}