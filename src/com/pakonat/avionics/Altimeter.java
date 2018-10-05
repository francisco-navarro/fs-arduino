package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;

public class Altimeter extends StepMotor {
	
	protected long lastAltitude = 0;
	
	protected final static String NAME = "altimeter";
	// one step => 4ms  
	protected int MAX_STEPS = Arduino.REFESH_RATE / 4;
	
	public Altimeter(Arduino arduino, FSUI fsui) throws Exception {
		
		super(arduino, fsui, NAME);
		lastAltitude = getAltitude ();
		initAltitude(lastAltitude);
	}

	private void initAltitude(long amount) throws Exception {
		int times = (int) Math.floor (amount / 100);
		int pos = 0;
		for(int i=0;i<times;i++) {
			pos +=100;
			writeStep((int) feetToStep(pos));
		}
		writeStep((int) feetToStep(amount));
	}

	@Override	
	public void update() throws Exception {
		long actual = getAltitude ();
		long feetDifference = actual - lastAltitude;
		long stepDifference = feetToStep(feetDifference);
		int symbol = 1;
		if (feetDifference < 0) symbol = -1;
		
		
		if (Math.abs(stepDifference) > 1) {
			int amountToWrite = (int) Math.min(Math.abs(stepDifference), MAX_STEPS) * symbol;
			System.out.println("actual alt " + lastAltitude + " -> " + (lastAltitude +stepToFeet(amountToWrite)) + " || write " + amountToWrite  + "steps");
			
			
			lastAltitude += stepToFeet(amountToWrite);
			writeStep((int)amountToWrite);
			Thread.sleep(10);
		}
		
	}
	
	private long stepToFeet (long n) {
		// una vuelta (1000ft) 520 pasos
		return Math.round(n / 0.52);
	}
	
	private long feetToStep (long n) {
		// una vuelta 520 (1000ft) pasos
		return (long) Math.floor(n * 0.52);
	}
	
	private long getAltitude () {
		long value = (long) ((fsui.readLong(memory)/65536 /65536) *3.28084);
		
		if(value>0 && value < 22000) {
			return value;
		}
		return 0;
	}
	
}
