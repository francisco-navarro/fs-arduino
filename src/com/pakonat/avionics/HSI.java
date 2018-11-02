package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;

public class HSI extends StepMotor {
	
	protected int last = 0;
	
	protected final static String NAME = "hsi";

	protected HSI(Arduino arduino, FSUI fsui, String name) {
		super(arduino, fsui, name);
	}
	
	
	

}
