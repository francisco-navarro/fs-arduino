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
		// Tenemos tres tramos de la recta para ajustar la velocidad
		double[] factor = getFactor(knots);
		
		// En value guardo 0.####
		value = (float) (knots * factor[0] + factor[1]);
		writeServo(Math.max(0,Math.round(value)));
	}
	
	public double[] getFactor (int knots) {
		if(knots<20) {
			return new double[] {0.3, 0};
		} else if(knots<60) {
			return new double[] {0.65, -8.333333333};
		} else if (knots<90) {
			return new double[] {1.192857143, -39.14285714};
		}
		return new double[] {0.8820740741, -8.842962963};
	}
	
}
