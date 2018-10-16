package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;

public class Altimeter extends StepMotor {
	
	protected long lastAltitude = 0;
	protected long lastSpeed = 0;
	
	protected final static String NAME = "altimeter";
	// one step => 4ms  
	protected int MAX_STEPS = Arduino.REFESH_RATE / 4;
	protected long[] LAST = {0,0,0,0};

	public Altimeter(Arduino arduino, FSUI fsui) throws Exception {
		
		super(arduino, fsui, NAME);
		lastAltitude = getAltitude ();
		initAltitude(lastAltitude);
	}

	private void initAltitude(long amount) throws Exception {
		int times = (int) Math.floor (amount / 1000);
		int pos = 0;
		for(int i=0;i<times;i++) {
			pos +=1000;
			writeStep((int) feetToStep(pos));
			Thread.sleep(2000);
		}
		writeStep((int) feetToStep(amount));
	}

	@Override	
	public void update() throws Exception {
		long actual = stepToFeet(feetToStep(getAltitude ()));
		long feetDifference = actual - lastAltitude;
		long stepDifference = feetToStep(feetDifference);
		int symbol = 1;
		if (feetDifference < 0) symbol = -1;
		
		addLastDifference(stepDifference);
		long newStepDifference = averageStepDifference(stepDifference);
		
		if (Math.abs(newStepDifference) > 1) {
			if(Math.abs(newStepDifference) > MAX_STEPS) {
				actual = lastAltitude + stepToFeet( MAX_STEPS*symbol);
			}
			System.out.println("actual alt " + lastAltitude + " -> " + actual + " || write " + actual  + "steps");

			writeStep((int)feetToStep(actual));
			lastAltitude = actual;
		} else {
			Thread.sleep(100);
		}
		lastSpeed = newStepDifference;
		
	}
	
	private long averageStepDifference(long stepDifference) {
		float average = LAST[0] + LAST[1] + LAST[2]+ LAST[3];
		
		return Math.round(average / 4.0) ;
	}

	private void addLastDifference(long stepDifference) {

		for(int i=0;i<LAST.length -1;i++) {
			LAST[i] = LAST[i+1];
		}
		LAST[LAST.length-1] = stepDifference;
	}

	private long stepToFeet (long n) {
		// una vuelta (1000ft) 520 pasosp
		return Math.round(n / 0.512);
	}
	
	private long feetToStep (long n) {
		// una vuelta 520 (1000ft) pasos
		return (long) Math.floor(n * 0.512);
	}
	
	private long getAltitude () {
		long value = (long) ((fsui.readLong(memory)/65536 /65536) *3.28084);
		
		if(value>0 && value < 22000) {
			return value;
		}
		return 0;
	}
	
}
