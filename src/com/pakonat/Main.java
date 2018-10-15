package com.pakonat;

import com.pakonat.avionics.Altimeter;
import com.pakonat.avionics.Attitude;
import com.pakonat.avionics.IAS;
import com.pakonat.avionics.VerticalSpeed;
import com.pakonat.things.Nav;
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
		
		new IAS(arduino, fsui);
		new VerticalSpeed(arduino, fsui);
		new Attitude(arduino, fsui);
		new Altimeter(arduino, fsui);
		//new Nav(arduino, fsui);
		
		arduino.start();
	}

}
