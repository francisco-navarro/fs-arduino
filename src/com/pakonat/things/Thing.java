package com.pakonat.things;

import com.pakonat.Arduino;

public abstract class Thing {
	
	Arduino arduino;
	
	Thing (Arduino arduino) {
		this.arduino = arduino;
	}
	
	protected synchronized void sendData(String data) throws InterruptedException{
		arduino.sendData(data);
	}

	public abstract void init() throws Exception;

	public abstract void update() throws Exception;

}
