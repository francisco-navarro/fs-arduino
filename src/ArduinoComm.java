
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
	
	private static final int WRITE_TIMEOUT = 40;
	
	// word write to arduino
	private static byte WRITE_TO = 119;
	private static byte SERVO_OFFSET = 48;
	
	private boolean servoStarted = false;
	
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
			sendData("a" + properties.getIAS());
			Thread.sleep(1000);
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
				if(inputLine.indexOf("attach") >= 0)
					servoStarted = true;
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
				
				while(!servoStarted) {					
					Thread.sleep(500);
				}
				
				while(keep) {
					
					
					writeServo(4,180);
					
					Thread.sleep(1);
					
					writeServo(5,177);
					
					Thread.sleep(150);
					
					writeServo(4,120);
					
					Thread.sleep(150);
					
					writeServo(4,90);
					
					Thread.sleep(150);
					
					writeServo(5,97);
					
					Thread.sleep(1);
					
					writeServo(4,30);
					
					Thread.sleep(150);
					
					writeServo(4,90);
					
					Thread.sleep(150);
					
					writeServo(4,120);
					
					Thread.sleep(1);
					
					writeServo(5,127);
					
					Thread.sleep(50);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void writeServo (int servo, int pos) throws Exception {
			byte[] data = {
					WRITE_TO,
					(byte) (servo + SERVO_OFFSET),
					(byte)(pos % 91 + 30),
					(byte)(pos > 91 ? 91 + 30 : 0)
			};

			
			sendData(new String(data));
			Thread.sleep(WRITE_TIMEOUT);
		}
		
		public void stop() {
			keep = false;
		}
		
	}

}



