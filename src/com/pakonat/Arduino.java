package com.pakonat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import com.pakonat.things.Thing;
import com.pakonat.util.PropertiesReader;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Arduino implements SerialPortEventListener {
	
	private OutputStream output = null;
	private BufferedReader input;
	private PropertiesReader properties;
	
	private static SerialPort serialPort;
	private static String PORT_NAME = "";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;
	
	public static final int REFESH_RATE = 60;

	/** Amount of servos to start */
	public static int servosStarted = 0;
	
	private ArrayList<Thing> things = new ArrayList<>();
	
	private MainLoop loop;
	private InputStreamReader inputStream;
	
	public Arduino(PropertiesReader prop) throws Exception {
		properties = prop;
		initializeArduinoConnection(prop);
	}
	
	public void start() {
		loop = new MainLoop();
		new Thread(loop).start();
	}
	
	public void addThing (Thing thing) {
		things.add(thing);
	}
	
	public void initializeArduinoConnection(PropertiesReader properties) throws Exception {

		CommPortIdentifier portId = getPort();

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open("ArduinoComm", TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			output = serialPort.getOutputStream();

			inputStream = new InputStreamReader(serialPort.getInputStream());
			input = new BufferedReader(inputStream);
			Thread.sleep(200);
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
					
			Thread.sleep(1000);
			if (input.ready()) {
			 emptyInput(input);
			}
			sendData(" ");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	private CommPortIdentifier getPort() throws Exception {
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		PORT_NAME = properties.getPortName();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				return currPortId;
			}
		}
		throw new Exception("Could not find COM port.");
	}

	private void emptyInput (BufferedReader input) {
		try {
			int count = 1;
			char buff[] = new char[2048];
			count = input.read(buff);
			System.out.print("Empty COM buffer..");
			
			while(count >= 2048) {
				System.out.print(".");
				count = input.read(buff);
			}
		}catch (Exception e) {
			System.err.println("WARN: Error trying to empty buffer");
		}
		
	}
	
	public synchronized void sendData(String data) throws InterruptedException{
		try {
			output.write((data + "\n").getBytes());
		} catch (IOException e) {
			System.err.println("Error sending data");
		}
	}
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				System.out.println(inputLine);
				if(inputLine.indexOf("attach") >= 0) {
					// Event of servo started
					servosStarted--;
				} else {
					for(Thing thing : things) {
						thing.receiveEvent(inputLine);
					}
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
	
	public synchronized void close() {
		if (loop != null) {
			loop.stop();
		}
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	class MainLoop implements Runnable {
		
		boolean keep = true;
		
		@Override
		public void run() {
			try {
				initializeThings();
				
				while(keep) {
					
//					while(servosStarted > 0) {					
//						Thread.sleep(500);
//					}
					for(Thing thing : things) {
						thing.update();
					}
					Thread.sleep(REFESH_RATE);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void stop() {
			keep = false;
		}
		
		private void initializeThings() throws Exception {
			for(Thing thing : things) {
				thing.init();
			}
		}
		
	}

	public String getProperty(String key){
		return properties.get(key);
	}
}
