
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ArduinoComm implements SerialPortEventListener {

	private OutputStream output = null;
	private BufferedReader input;

	private static SerialPort serialPort;
	private static String PORT_NAME = "";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 19200;
	
	private PropertiesReader properties;
	

	public static void main(String[] args) throws Exception {		
		new ArduinoComm();
	}
	
	ArduinoComm () throws Exception {
		properties = new PropertiesReader();
		
		initializeArduinoConnection(properties);
		
		MainLoop loop = new MainLoop();
		new Thread(loop).start();
	}

	private void initializeServos() throws InterruptedException {
		if (properties.getVerticalSpeed() != null) {
			sendData("a" + properties.getVerticalSpeed());
		}
	}

	public void initializeArduinoConnection(PropertiesReader properties) throws Exception {

		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		PORT_NAME = properties.getPortName();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				portId = currPortId;
				break;
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open("ArduinoComm", TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			output = serialPort.getOutputStream();

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
//						
			Thread.sleep(400);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	private void sendData(String data) throws InterruptedException{
		 
		try {
			output.write((data + "\n").getBytes());
		} catch (IOException e) {
			System.err.println("Error sending data");
		}
//		try {
//			String inputLine=input.readLine();
//			if (inputLine.length() > 2)
//				System.out.println(inputLine);
//		} catch (Exception e) {
//			System.err.println(e.toString());
//		}
	}
	
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	

	class MainLoop implements Runnable {
		
		boolean keep = true;
		
		@Override
		public void run() {

			try {
				Thread.sleep(1500);
				initializeServos();
				Thread.sleep(500);
				
				while(keep) {
					sendData("w42");
					Thread.sleep(40);
					sendData("w4kk");
					Thread.sleep(40);
					sendData("w42");
					Thread.sleep(40);
					sendData("w4kk");
					Thread.sleep(40);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void stop() {
			keep = false;
		}
		
	}

}



