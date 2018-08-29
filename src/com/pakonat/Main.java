package com.pakonat;

import com.pakonat.things.Servo;
import com.pakonat.util.PropertiesReader;

public class Main {
	
	PropertiesReader prop;
	Arduino arduino;
	FSUI fsui;
	
	public static void main (String args[]) throws Exception {
		new Main();
	}

	private Main() throws Exception {
		prop = new PropertiesReader();
		arduino = new Arduino(prop);
		fsui = new FSUI(prop);
		
		new Servo(arduino, fsui, "ias");
		
		arduino.start();
	}

}
