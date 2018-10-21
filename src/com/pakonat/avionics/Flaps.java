package com.pakonat.avionics;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.Servo;

public class Flaps extends Servo {
	
	float lastValue = 0;
	float max = 16383.0f;
	int increment = 0;

	public Flaps(Arduino arduino, FSUI fsui) {
		super(arduino, fsui, "flaps");
		increment = fsui.getShort(Integer.parseInt("3BFA", 16));
	}
	
	@Override
	public void update() throws Exception {
		long actual = fsui.readInt(memory);
		value = (float) Math.ceil(actual / max * 100);
		if(value != lastValue) {
			float deg =  value/100*38;
			writeServo((int) Math.ceil(deg));
			lastValue = value;
		}
	}
	
	@Override
	public void receiveEvent(String str) throws Exception {
		if(str.indexOf("Flaps") != -1) {
			int amount = Integer.parseInt(str.replaceFirst(".*:", ""));
			long actual = fsui.readInt(Integer.parseInt("0BDC", 16));
			
			if (amount <= 49) {
				System.out.println("30%");
				fsui.writeLong(Integer.parseInt("0BDC", 16), 9300);
			} else if (amount < 54) {
				System.out.println("20%");
				fsui.writeLong(Integer.parseInt("0BDC", 16), increment);
			} else if (amount<56) {
				System.out.println("10%");
				fsui.writeLong(Integer.parseInt("0BDC", 16), 1);
			} else {
				System.out.println("0%");
				fsui.writeLong(Integer.parseInt("0BDC", 16), -12000);
			}
			
		}
	}

}
