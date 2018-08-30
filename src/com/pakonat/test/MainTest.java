package com.pakonat.test;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.Main;
import com.pakonat.avionics.IAS;
import com.pakonat.avionics.VerticalSpeed;
import com.pakonat.util.PropertiesReader;

public class MainTest {
	
	PropertiesReader prop;
	Arduino arduino;
	FSUI fsui;
	
	public static void main (String args[]) throws Exception {
		prop = new PropertiesReader();
		arduino = new Arduino(prop);
		fsui = new FSUIMock(prop);
		
		new IAS(arduino, fsui);
		new VerticalSpeed(arduino, fsui);
		
		arduino.start();
	}
	
	public class FSUIMock extends FSUI {

		public FSUIMock(PropertiesReader prop) {
			super(prop);
		}
		
	}

}
