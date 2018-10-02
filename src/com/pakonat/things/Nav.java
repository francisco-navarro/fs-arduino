package com.pakonat.things;

import java.nio.ByteBuffer;

import com.flightsim.fsuipc.FSNav1;
import com.flightsim.fsuipc.FSNavRadio;
import com.pakonat.Arduino;
import com.pakonat.FSUI;

public class Nav extends Thing {
	
	protected int memory = Integer.parseInt("311E", 16);
	protected FSNavRadio nav;

	public Nav(Arduino arduino, FSUI fsui) {
		super(arduino, fsui);
		// TODO Auto-generated constructor stub
		arduino.addThing(this);
		 nav = new FSNav1();
	}

	@Override
	public void init() throws Exception {
	}
 
	@Override
	public void update() throws Exception {
		
	}
	
	@Override
	public void receiveEvent(String str) throws Exception {
		if (str.indexOf("encoder") != -1) {
			short uValue = fsui.getShort(memory);
			short value = fromUnsigned(uValue);
			
			if (str.indexOf("+") != -1) {
				uValue +=5;
				value +=5;
			} else if (str.indexOf("-") != -1) {
				uValue -=5;
				value -=5;
			}
			short newValue = toUnsigned(value);
			System.out.println(fromUnsigned(newValue));
			
			fsui.writeShort(memory, newValue);
		}
	}

	private short toUnsigned(short value) {
		ByteBuffer buf = ByteBuffer.allocate(2);
		byte[] data = new byte[2];
		short dig1 = (short) (value / 1000);
		value = (short) (value - dig1*1000);
		short dig2 = (short) (value / 100);
		value = (short) (value - dig2*100);
		short dig3 = (short) (value / 10);
		short dig4 = (short) (value % 10);
		
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