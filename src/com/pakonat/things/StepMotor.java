package com.pakonat.things;

import java.nio.ByteBuffer;

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
		sendData("----".getBytes());
	}

	@Override
	public void update() throws Exception {
		
	}
	
	public void writeStep (int amount) throws Exception {
//		byte[] data = {
//				WRITE_TO,
//				(byte) (this.port + SERVO_OFFSET)
//		};
//		sendData(new String(data) + new String(toBuff(amount)));
		
		ByteBuffer bb = ByteBuffer.allocate(6).put(("s" +port).getBytes());
		bb.putInt(2, amount);
		sendData(bb.array());
		Thread.sleep(WRITE_TIMEOUT);
	}
	
	public static byte[] toBuff(int a) {
		byte[] buf = new byte[4];
		buf[0] = (byte)(a>>12 & 0x0f);
		buf[1] = (byte)(a>>8 & 0x0f);
		buf[2] = (byte)(a>>4 & 0x0f);
		buf[3] = (byte) (a& 0x0f);
		return buf;
	}
}
