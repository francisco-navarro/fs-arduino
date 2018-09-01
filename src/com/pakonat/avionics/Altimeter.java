package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;

public class Altimeter extends StepMotor {
	
	protected long lastAltitude = 0;
	
	protected final static String NAME = "altimeter";
	
	protected long FEET_BY_STEP = 5;
	
	// one step => 4ms  
	protected int MAX_STEPS = Arduino.REFESH_RATE / (4 + 2);
	
	public Altimeter(Arduino arduino, FSUI fsui) {
		
		super(arduino, fsui, NAME);
		lastAltitude = getAltitude ();
	}

	@Override	
	public void update() throws Exception {
		long actual = getAltitude ();
		long difference = actual - lastAltitude;
		
		
		if (Math.abs(difference / FEET_BY_STEP) > 0) {
			int amountToWrite = (int) Math.min(difference / FEET_BY_STEP, MAX_STEPS);
			
			System.out.println("actual " + lastAltitude + " -> " + actual + " || write " + amountToWrite + "steps");
			
			
			lastAltitude += amountToWrite * FEET_BY_STEP;
			writeStep( amountToWrite);
		}
		
	}
	
	private long getAltitude () {
		long value = (long) ((fsui.readLong(memory)/65536 /65536) *3.28084);
		
		if(value>0 && value < 22000) {
			return value;
		}
		return 0;
	}
	
}
