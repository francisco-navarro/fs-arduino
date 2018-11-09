package com.pakonat.avionics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.flightsim.fsuipc.FSAircraft;
import com.flightsim.fsuipc.FSNavRadio;
import com.flightsim.fsuipc.fsuipc_wrapper;
import com.pakonat.Arduino;
import com.pakonat.FSUI;
import com.pakonat.things.StepMotor;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;

public class HSI extends StepMotor {
	
	protected FSAircraft aircraft = new FSAircraft();
	
	public static short lastOBS = 0;
	public static int lastDeviation = 90;
	public static double lastHeading = 0;
	
	protected final static String NAME = "hsi";
	
	protected final int memoryCourseOBS = Integer.parseInt("0C4E", 16);
	protected final int memoryCourseDeviation = Integer.parseInt("2AAC", 16);
	protected final int memoryHeading = Integer.parseInt("0580", 16);
	
	// pins
	protected final int servoDeviation = 6;
	protected final int pinHeading = 34;
	
	protected final short encoderOBS = 30;

	public HSI(Arduino arduino, FSUI fsui) {
		super(arduino, fsui, "hsi");
	}
	
	
	public void update() throws Exception {
		
		short actualOBS = fsui.getShort(memoryCourseOBS);
		if(lastOBS!=actualOBS) {
			lastOBS=actualOBS;
		}
		double factor = 3600.0/(65536.0*65536);
		
		double heading = Math.round(getUInt(0x0580)*factor)/10.0;
		if(lastHeading!=heading) {
			
			lastHeading= heading;
//			writeHeading((int) heading);
		}
		
		writeDeviation(Math.round(fsui.getFloat(memoryCourseDeviation)/10));
	}
	
	private void writeHeading(int heading) throws Exception {
		byte[]  order = {"h".getBytes()[0],
	             (byte) (this.pinHeading + 48)};
			ByteBuffer bb = ByteBuffer.allocate(6).put(order);
			bb.putInt(2, heading);
			sendData(bb.array());
			Thread.sleep(WRITE_TIMEOUT);
	}


	public void receiveEvent(String str) throws Exception {
		if (str.matches("\\[encoder.*")) {
			int value = lastOBS;
			if(str.matches(".*\\+\\]")) {
				value++;
			} else if(str.matches(".*\\-\\]")) {
				value--;
			}
			
			if (value < 0) {
				value +=360;
			}
			if (value > 360) {
				value -=360;
			}
			
			fsui.writeShort(memoryCourseOBS, (short) value);
		}
	}
	

	private void writeDeviation(int value) throws Exception {
		int pos = 92 + value * 2;
		
		if(lastDeviation != pos) {
			pos = Math.min(150, pos);
			pos = Math.max(60, pos);
			lastDeviation = pos;
			byte[] data = {
					119,
					(byte) (servoDeviation + SERVO_OFFSET),
					(byte)(pos % 91 + 30),
					(byte)(pos >= 91 ? 91 + 30 : 0)
			};
//			sendData(data);
//			Thread.sleep(WRITE_TIMEOUT);
		}
	}

	public long getUInt(int aOffset)
	{
		byte[] data = new byte[8];
		fsuipc_wrapper.ReadData(aOffset,8,data);
		long x = java.nio.ByteBuffer.wrap(data).order(java.nio.ByteOrder.LITTLE_ENDIAN).getLong();
	    return x & 0xffffffffL;
	}
}
