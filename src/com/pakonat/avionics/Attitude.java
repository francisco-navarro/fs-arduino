package com.pakonat.avionics;

import com.flightsim.fsuipc.FSAircraft;
import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.Servo;

public class Attitude extends Servo{
	
	protected double verticalSpeed;
	
	private final static String NAME = "attitude.indicator";
	
	protected int zeroPoint;
	
	protected FSAircraft aircraft = new FSAircraft();
	
	protected int PITCH_PORT = 4;
	protected int BANK_PORT = 3;

	public Attitude(Arduino arduino, FSUI fsui) throws Exception {
		super(arduino, fsui, NAME);
		writeServo(PITCH_PORT, 90);
		writeServo(BANK_PORT, 90);
		
		arduino.addThing(this);
	}
	
	@Override
	public void update() throws Exception {
		
		
		int pitch = (int) Math.round(aircraft.Pitch());
		int bank = (int) Math.round(aircraft.Bank());
		// Viene en grados, 40 maximo, -40 minimo
		
		// 40 es 120
		int degreesP = (pitch * 60 / 40) + 90;
		int degreesB = (bank * 60 / 40) + 90;
		
		
		writeServo(PITCH_PORT, degreesP);
		writeServo(BANK_PORT, degreesB);
	}

	public void writeServo (int port, int pos) throws Exception {
		if(last != pos) {
			last = pos;
			byte[] data = {
					WRITE_TO,
					(byte) (port + SERVO_OFFSET),
					(byte)(pos % 91 + 30),
					(byte)(pos >= 91 ? 91 + 30 : 0)
			};
			sendData(data);
			Thread.sleep(WRITE_TIMEOUT);
		}
	}
}
