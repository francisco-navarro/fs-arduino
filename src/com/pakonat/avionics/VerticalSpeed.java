package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.Servo;

public class VerticalSpeed extends Servo {
	
	protected double verticalSpeed;
	
	private final static String NAME = "vertical.speed";
	
	protected int zeroPoint;

	public VerticalSpeed(Arduino arduino, FSUI fsui) {
		super(arduino, fsui, NAME);
		max = 24;
		zeroPoint = Integer.parseInt(arduino.getProperty(name + ".zero.point"));
	}

	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		// El valor en nudos viene * 128
		verticalSpeed = actual *60*3.28084/256/100;
		// En value guardo 0.####
		value =  (float) verticalSpeed / max;
		
		writeServo(Math.round(zeroPoint + value * 90));
	}
	
}
