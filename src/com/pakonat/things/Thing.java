package com.pakonat.things;

import com.pakonat.Arduino;
import com.pakonat.FSUI;

public abstract class Thing {
	
	protected Arduino arduino;

	protected FSUI fsui;
	
	Thing (Arduino arduino, FSUI fsui) {
		this.arduino = arduino;
		this.fsui = fsui;
	}
	
	protected synchronized void sendData(String data) throws InterruptedException{
		arduino.sendData(data);
	}

	public abstract void init() throws Exception;

	public abstract void update() throws Exception;

}
