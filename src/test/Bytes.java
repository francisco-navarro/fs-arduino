package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	private static String PORT_NAME = "COM4";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;
	
	
	public static void main(String[] args) throws Exception {
		int start = 518/2;
		
		Bytes instance = new Bytes();
		
		instance.initializeArduinoConnection();
		Thread.sleep(1510);
		
		
		instance.sendData("s144".getBytes());
		ByteBuffer bb = ByteBuffer.allocate(6).put("sF".getBytes());
		bb.putInt(2, start);
		instance.sendData(bb.array());
		Thread.sleep(3200);
		
//		for(int i=0; start>120;start-=5) {
//			i++;
//			bb = ByteBuffer.allocate(6).put("sF".getBytes());
//			bb.putInt(2, start + 1);
//			instance.sendData(bb.array());
//			Thread.sleep(80);
//		}
//		for(start = 120; start<520;start+=6) {
//			bb = ByteBuffer.allocate(6).put("sF".getBytes());
//			bb.putInt(2, start + 1);
//			instance.sendData(bb.array());
//			Thread.sleep(80);
//		}
		for(; start>0;start-=5) {
			bb = ByteBuffer.allocate(6).put("sF".getBytes());
			bb.putInt(2, start);
			instance.sendData(bb.array());
			Thread.sleep(80);
			bb.putInt(2, start + 3);
			instance.sendData(bb.array());
			Thread.sleep(120);
			bb.putInt(2, start - 2);
			instance.sendData(bb.array());
			Thread.sleep(80);
		}
//		for(; start<520;start+=3) {
//			bb = ByteBuffer.allocate(6).put("sF".getBytes());
//			bb.putInt(2, start + 1);
//			instance.sendData(bb.array());
//			Thread.sleep(80);
//		}
		Thread.sleep(550);
		 
		start = 0;
		System.exit(0);
		
	}
	
	public static void print (byte[] bb) {
		for(int i=2; i<bb.length;i++) {
			byte b = bb[i];
            String bits = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.print(bits);
            System.out.println(" (" + b + ")");
        }
	}
	
	public static byte[] toBuff(int i) {
		byte[] buf = new byte[4];
		buf[0] = (byte)(i>>24& 0x0f);
		buf[1] = (byte)(i>>16& 0x0f);
		buf[2] = (byte)(i>>8& 0x0f);
		buf[3] = (byte) (i);
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

			
			if (input.ready()) {
			 emptyInput(input);
			}
			sendData(" ".getBytes());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			sendData(" ".getBytes());
			throw e;
		}
	}

	private CommPortIdentifier getPort() throws Exception {
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		PORT_NAME = "COM4";

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

			if (PORT_NAME.equals(currPortId.getName())) {
				return currPortId;
			}
		}
		throw new Exception("Could not find COM port.");
	}
	
	public synchronized void sendData(byte[] data) throws InterruptedException{
		try {
			ByteBuffer bb = ByteBuffer.allocate(data.length + 1).put(data);
			bb.put(data.length, "\n".getBytes()[0]);
			output.write(bb.array());
		} catch (IOException e) {
			System.err.println("Error sending data");
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				System.out.println("|  " + inputLine);
				
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
