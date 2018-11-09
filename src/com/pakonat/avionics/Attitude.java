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
		
		
		double pitch = aircraft.Pitch() * -1;
		double bank = aircraft.Bank() *-1;
		// Viene en grados, 40 maximo, -40 minimo
		
		// El 90 y el 86 son los puntos 0
		int degreesP = (int) Math.round((pitch * 1) + 90);
		int degreesB = (int) Math.round((bank * 1.4) + 102);
		
		
		writeServo(PITCH_PORT, degreesP);
		writeServo(BANK_PORT, degreesB);
	}

	public void writeServo (int port, int pos) throws Exception {
		if(Math.abs(pos) < 120 &&  Math.abs(pos) > 60) {
			last = pos;
			byte[] data = {
					WRITE_TO,
					(byte) (port + SERVO_OFFSET),
					(byte)(pos % 91 + 30),
					(byte)(pos >= 91 ? 91 + 30 : 0)
			};
			sendData(data);
			//System.out.println(new String(data));
			Thread.sleep(WRITE_TIMEOUT);
		}
	}
}
