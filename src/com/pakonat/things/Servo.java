package com.pakonat.things;

import com.pakonat.Arduino;
import com.pakonat.FSUI;

public class Servo extends Thing {

	/** Order write servo arduino */
	protected static byte WRITE_TO = 119;
	
	/** Order attach servo arduino */
	protected static String ATTACH = "a";

	protected static final long WRITE_TIMEOUT = 1;
	 
	protected int port;
	
	protected int max, min;
	
	protected int maxServo = 180;
	
	protected int memory;
	
	protected float value = 0;
	
	protected float last = 0;
	
	protected String name;
	
	
	public Servo(Arduino arduino, FSUI fsui, String name) {
		super(arduino, fsui);
		this.name = name;
		String port = arduino.getProperty(name + ".pin");
		String max = arduino.getProperty(name + ".max");
		String min = arduino.getProperty(name + ".min");
		String memory = arduino.getProperty(name + ".memory");
		
		if (port != null ) {
			this.name = name;
			this.port = Integer.parseInt(port);
			this.memory = Integer.parseInt(memory, 16);
			arduino.addThing(this);
			arduino.servosStarted++;
		}
	}

	@Override
	public void init() throws Exception {
		sendData(new String(ATTACH + this.port).getBytes());
		Thread.sleep(WRITE_TIMEOUT);
	}

	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		// En value guardo 0.####
		value = (float)(actual - min) / (max - min);
		writeServo(180 - Math.round(180*value));
	}

	public void writeServo (int pos) throws Exception {
		if(last != pos) {
			last = pos;
			byte[] data = {
					WRITE_TO,
					(byte) (this.port + SERVO_OFFSET),
					(byte)(pos % 91 + 30),
					(byte)(pos >= 91 ? 91 + 30 : 0)
			};
			sendData(data);
			Thread.sleep(WRITE_TIMEOUT);
		}
	}

}
