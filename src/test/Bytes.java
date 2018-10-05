package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Enumeration;

import com.pakonat.things.Thing;
import com.pakonat.util.PropertiesReader;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Bytes implements SerialPortEventListener { 

	private OutputStream output = null;
	private BufferedReader input;
	private InputStreamReader inputStream;
	
	private static SerialPort serialPort;
	private static String PORT_NAME = "COM3";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;
	
	
	public static void main(String[] args) throws Exception {
		int start = 520;
		
		Bytes instance = new Bytes();
		
		instance.initializeArduinoConnection();
		Thread.sleep(1000);
		
		instance.sendData("_");
		Thread.sleep(1000);
		instance.sendData("_");
		Thread.sleep(300);
		instance.sendData("s9" + new String(toBuff(start)));
		
		Thread.sleep(300);
		instance.sendData("_");
		
		
		
		
		for(int i=0; i<100;i++) {
			Thread.sleep(150);
			instance.sendData("s9" + new String(toBuff(start+i)));
		}
	}
	
	public static byte[] toBuff(int a) {
		byte[] buf = new byte[4];
		buf[0] = (byte)(a>>12 & 0x0f);
		buf[1] = (byte)(a>>8 & 0x0f);
		buf[2] = (byte)(a>>4 & 0x0f);
		buf[3] = (byte) (a& 0x0f);
		return buf;
	}
	
	public void initializeArduinoConnection() throws Exception {

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
		PORT_NAME = "COM3";

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				return currPortId;
			}
		}
		throw new Exception("Could not find COM port.");
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
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
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
	
	
}
