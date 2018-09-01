package com.pakonat.things;

import com.pakonat.Arduino;
import com.pakonat.FSUI;

public class StepMotor extends Thing {
	
	// Order for arduino
	protected static byte WRITE_TO = 115;
	
	protected static final int WRITE_TIMEOUT = 2;
	
	protected int memory;
	
	protected long position;
	
	protected int port, velocity;
	
	protected String name;

	protected StepMotor(Arduino arduino, FSUI fsui, String name) {
		super(arduino, fsui);
		position = 0;
		
		String port = arduino.getProperty(name + ".pin");
		String velocity = arduino.getProperty(name + ".velocity");
		String memory = arduino.getProperty(name + ".memory");
		
		
		if (port != null ) {
			this.name = name;
			this.port = Integer.parseInt(port);
			this.memory = Integer.parseInt(memory, 16);
			this.velocity = Integer.parseInt(velocity);
			arduino.addThing(this);
		}
	}

	@Override
	public void init() throws Exception {
		sendData("----");
	}

	@Override
	public void update() throws Exception {
		
	}
	
	public void writeStep (int pos) throws Exception {
		byte[] data = {
				WRITE_TO,
				(byte) (this.port + SERVO_OFFSET),
				(byte)(pos + 64),
		};
		sendData(new String(data));
		Thread.sleep(WRITE_TIMEOUT);
	}

}
