
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class ArduinoComm {

	private static OutputStream output = null;

	private static SerialPort serialPort;
	private static String PORT_NAME = "";
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public static void main(String[] args) throws InterruptedException {
		if(args.length>0) {
			// example "/dev/cu.usbmodem14231"
			PORT_NAME = args[0];
		}
		
		initializeArduinoConnection();
		sendData("90\n");
		Thread.sleep(1000);
		sendData("1\n");
		Thread.sleep(1300);
		sendData("2\n");
		Thread.sleep(1300);
		sendData("90\n");
		Thread.sleep(1300);
		sendData("180\n");
		Thread.sleep(1000);
		sendData("1\n");
		System.exit(0);
	}

	public static void initializeArduinoConnection() {

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

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

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void sendData(String data){
		 
		try {
			output.write(data.getBytes());
		} catch (IOException e) {
			System.out.println("Error sending data");
		}
	}
}
