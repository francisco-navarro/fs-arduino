package com.pakonat.test;

import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.Main;
import com.pakonat.avionics.Altimeter;
import com.pakonat.avionics.IAS;
import com.pakonat.avionics.VerticalSpeed;
import com.pakonat.util.PropertiesReader;

public class MainTest {

	PropertiesReader prop;
	Arduino arduino;
	FSUI fsui;

	public static void main(String args[]) throws Exception {
		new MainTest();
	}

	MainTest() throws Exception {
		prop = new PropertiesReader();
		arduino = new ArduinoMock(prop);
		fsui = new FSUIMock(prop);

		new Altimeter(arduino, fsui);
		// new IAS(arduino, fsui);

		arduino.start();
	}

	public class ArduinoMock extends Arduino {

		public ArduinoMock(PropertiesReader prop) throws Exception {
			super(prop);
		}

//		@Override
//		public void initializeArduinoConnection(PropertiesReader properties) throws Exception {
////			super(properties);
//		}

	}

	public class FSUIMock extends FSUI {

		int value = 100;
		int max = 3000;
		int min = 100;
		int direction = 1;
		int step = 1;

		public FSUIMock(PropertiesReader prop) {
			ChangeValueThread thread = new ChangeValueThread();
			thread.start();
		}

		@Override
		public long readLong(int position) {
			return value;
		}

		@Override
		public int readInt(int position) {
			return value;
		}

		public class ChangeValueThread extends Thread {

			public void run() {
				try {
					
					Thread.sleep(5000);

					while (true) {
						value = value + direction * step;
						if (value <= min || value >= max) {
							direction *= -1;
							Thread.sleep(500);
						}
						Thread.sleep((int) Math.random() * 500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

}
