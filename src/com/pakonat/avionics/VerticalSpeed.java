package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.Servo;

public class VerticalSpeed extends Servo {
	
	protected double verticalSpeed;
	
	private final static String NAME = "vertical.speed";

	public VerticalSpeed(Arduino arduino, FSUI fsui) {
		super(arduino, fsui, NAME);
	}

	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		// El valor en nudos viene * 128
		verticalSpeed = actual *60*3.28084/256/100;
		// En value guardo 0.####
		value =  (float) Math.abs(verticalSpeed / max);
		
		writeServo(Math.min(180,
				Math.round(90 + 90 * value * (verticalSpeed<0?1:-1)))
				);
	}
	
}
