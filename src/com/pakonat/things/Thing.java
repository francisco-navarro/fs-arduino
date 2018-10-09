package com.pakonat.things;

import com.pakonat.Arduino;
import com.pakonat.FSUI;

public abstract class Thing {
	
	protected Arduino arduino;

	protected FSUI fsui;

	/** Bytes to subtract to servo number (for arduino interpeter) */
	protected static byte SERVO_OFFSET = 48;
	
	Thing (Arduino arduino, FSUI fsui) {
		this.arduino = arduino;
		this.fsui = fsui;
	}
	
	protected synchronized void sendData(byte[] data) throws InterruptedException{
		arduino.sendData(data);
	}

	public abstract void init() throws Exception;

	public abstract void update() throws Exception;
	
	public void receiveEvent(String str) throws Exception {
		
	}

}
