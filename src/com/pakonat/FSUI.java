package com.pakonat;

import com.flightsim.fsuipc.FSFlightSim;
import com.flightsim.fsuipc.FSUIPC;
import com.flightsim.fsuipc.fsuipc_wrapper;
import com.pakonat.util.PropertiesReader;

public class FSUI {
	
	private FSFlightSim sim;
	private FSUIPC fsui;
	private int ret = 0;
	
	public FSUI() {
		
	}

	public FSUI(PropertiesReader prop) {
		ret = fsuipc_wrapper.Open(fsuipc_wrapper.SIM_ANY);
		System.out.println("ret =" + ret);
		if(ret == 0 ) {
			System.out.println("Flight sim not found");
		} else {			
			sim = new FSFlightSim();
			fsui = new FSUIPC();
			System.out.println("name " + sim.StartSituationName());
		}
	}
	
	public long readLong(int position) {
		return fsui.getLong(position);
	}
	
	public int readInt(int position) {
		return fsui.getInt(position);
	}
	
	public short getShort(int position) {
		return fsui.getShort(position);
	} 
	
	public float getFloat(int position) {
		return fsui.getFloat(position);
	} 

	public short readShort(int position) {
		return fsui.getShort(position);
	} 
	
	public long readU32(int aOffset)
	{
		byte[] data = new byte[8];
		fsuipc_wrapper.ReadData(aOffset,8,data);
		long x = java.nio.ByteBuffer.wrap(data).order(java.nio.ByteOrder.LITTLE_ENDIAN).getLong();
	    return x & 0xffffffffL;
	}

	public void writeShort(int memory, short uValue) {
		byte[] data = new byte[2];
		
		data[0] = (byte) (uValue & 0xff);
		data[1] = (byte) ((uValue >>8) & 0xff);
		fsuipc_wrapper.WriteData(memory,2,data);
	}
	
	public void writeInt(int memory, int uValue) {
		byte[] data = new byte[4];
		
		data[0] = (byte) (uValue & 0xff);
		data[1] = (byte) ((uValue >>8) & 0xff);
		data[2] = (byte) ((uValue >>16) & 0xff);
		data[3] = (byte) ((uValue >>24) & 0xff);
		fsuipc_wrapper.WriteData(memory,4,data);
	}
	
	public void writeLong(int memory, int uValue) {
		byte[] data = new byte[6];
		
		data[0] = (byte) (uValue & 0xff);
		data[1] = (byte) ((uValue >>8) & 0xff);
		data[2] = (byte) ((uValue >>16) & 0xff);
		data[3] = (byte) ((uValue >>24) & 0xff);
		data[4] = (byte) ((uValue >>32) & 0xff);
		data[5] = (byte) ((uValue >>40) & 0xff);
		fsuipc_wrapper.WriteData(memory,4,data);
	}


}
