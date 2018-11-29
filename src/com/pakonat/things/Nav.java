package com.pakonat.things;

import java.nio.ByteBuffer;

import com.flightsim.fsuipc.FSNav1;
import com.flightsim.fsuipc.FSNavRadio;
import com.pakonat.Arduino;
import com.pakonat.FSUI;

public class Nav extends Thing {
	
	protected static byte WRITE_TO = 110;
	protected int[] memoryNav = {
			Integer.parseInt("311E", 16), // nav1 stby
			Integer.parseInt("0350", 16), // nav1 
			Integer.parseInt("311A", 16), // comn1 stby
			Integer.parseInt("034E", 16) // com1 
	};
	protected FSNavRadio nav;
	
	private final short ZERO = 0;
	
	protected short[] lastRead = {0, 0, 0, 0};

	public Nav(Arduino arduino, FSUI fsui) {
		super(arduino, fsui);
		// TODO Auto-generated constructor stub
		arduino.addThing(this);
		nav = new FSNav1();
	}

	@Override
	public void init() throws Exception {
		
		
		update();
		nav.FreqAsString();
		System.out.println(nav.FreqAsString());
	}

	private short readRadio(int offset) {
		short uValue = fsui.getShort(offset);
		short value = fromUnsigned(uValue);
		return value;
	}
	
	
	
	public void send(int n, short value) throws InterruptedException {
		short value1 =  (short) (value / 100);
		short value2 =  (short) (value - value1 * 100);
		byte[] data = {
				WRITE_TO,
				(byte) Integer.toString(n +1 ).charAt(0),
				(byte)value1,
				(byte)value2
		};
		sendData(data);
		sendData("\n".getBytes());
	}
 
	@Override
	public void update() throws Exception {
		
		short value;
		
		for(int i=0; i<4; i++) {
			value = readRadio(memoryNav[i]);
			if (value != lastRead[i]) {
				lastRead[i] = value;
				send(i, value);
			}
		}
	}
	
	@Override
	public void receiveEvent(String str) throws Exception {
		if (str.indexOf("encoder") != -1) {
//			short uValue = fsui.getShort(memory);
//			short value = fromUnsigned(uValue);
//			
//			if (str.indexOf("+") != -1) {
//				uValue +=5;
//				value +=5;
//			} else if (str.indexOf("-") != -1) {
//				uValue -=5;
//				value -=5;
//			}
//			short newValue = toUnsigned(value);
//			System.out.println(fromUnsigned(newValue));
//			
//			fsui.writeShort(memory, newValue);
		}
	}

	private short toUnsigned(short value) {
		ByteBuffer buf = ByteBuffer.allocate(2);
		byte[] data = new byte[2];
		int dig1 = value / 1000;
		int dig2 = (value%1000) / 100;
		int dig3 = (value%100) / 10;
		int dig4 = value % 10;
		
		data[0] = (byte) (dig1<<4 | dig2);
		data[1] = (byte) (dig3<<4 | dig4);
		buf.put(data,0,2);
		return buf.getShort(0);
	}

	private short fromUnsigned(short value) {
		int dig1 = (value>>12 & 0x0f);
		int	dig2 = (value>>8 & 0x0f);
		int dig3 = (value>>4 & 0x0f);
		int dig4 = (value& 0x0f);
		
		return (short) (dig1 * 1000 + dig2 * 100 + dig3 * 10 + dig4);
	}

}
