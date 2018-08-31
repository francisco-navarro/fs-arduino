package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;

public class Altimeter extends StepMotor {
	
	protected long lastAltitude = 0;
	
	protected final static String NAME = "altimeter";
	
	protected long FEET_BY_STEP = 100;
	
	// one step => 4ms  
	protected int MAX_STEPS = Arduino.REFESH_RATE / (4 - 1);
	
	public Altimeter(Arduino arduino, FSUI fsui, String name) {
		
		super(arduino, fsui, name);
		lastAltitude = fsui.readInt(memory);
	}

	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		long difference = actual - lastAltitude;
		
		if (Math.abs(difference) > FEET_BY_STEP) {
			System.out.println("actual " + actual + " last " + lastAltitude);
			System.out.println("difference " + difference);
			
			int amountToWrite = (int) Math.min(difference / FEET_BY_STEP, MAX_STEPS);
			
			lastAltitude += amountToWrite * FEET_BY_STEP;
			writeStep( amountToWrite);
		}
		
	}
	
}
