package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.Servo;

public class IAS extends Servo {
	
	protected int knots;
	
	private final static String NAME = "ias";

	public IAS(Arduino arduino, FSUI fsui) {
		super(arduino, fsui, NAME);
	}

	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		// El valor en nudos viene * 128
		knots = (int) Math.round(actual / 128);
		// En value guardo 0.####
		value = (float) 100 * (knots - min) / (max - min);
		writeServo(180 - Math.round(maxServo*value/100));
	}
	
}
