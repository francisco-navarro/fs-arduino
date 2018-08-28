
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class ArduinoComm {

	private static OutputStream output = null;
	private static BufferedReader input;

	private static SerialPort serialPort;
	private static String PORT_NAME = "";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	
	// try http://www.ardulink.org/to-retrieve-an-ardulink-2-link/

	public static void main(String[] args) throws Exception {		
		PropertiesReader properties = new PropertiesReader();
		
		initializeArduinoConnection(properties);
		initializeServos(properties);
		
		mainLoop(properties);
	}

	private static void initializeServos(PropertiesReader properties) throws InterruptedException {
		if (properties.getVerticalSpeed() != null) {
			sendData("ATT;" + properties.getVerticalSpeed());
			Thread.sleep(200);
		}
	}

	private static void mainLoop(PropertiesReader properties) throws Exception {
		sendData("WSR;"+properties.getVerticalSpeed()+";90");
		Thread.sleep(200);
		sendData("WSR;"+properties.getVerticalSpeed()+";002");
		Thread.sleep(200);
		sendData("WSR;"+properties.getVerticalSpeed()+";90");
		Thread.sleep(200);
		sendData("WSR;"+properties.getVerticalSpeed()+";180");
		Thread.sleep(1200);
		System.exit(0);
	}

	public static void initializeArduinoConnection(PropertiesReader properties) {

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
			
			Thread.sleep(500);
			read();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void sendData(String data){
		 
		try {
			output.write((data + "\n").getBytes());
			read();
			
		} catch (IOException e) {
			System.err.println("Error sending data");
		}
	}
	
	private synchronized static void read() {
		try {
			String inputLine=input.readLine();
			System.out.println(inputLine);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
