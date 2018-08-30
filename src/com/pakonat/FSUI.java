package com.pakonat;

import com.flightsim.fsuipc.FSFlightSim;
import com.flightsim.fsuipc.FSUIPC;
import com.flightsim.fsuipc.fsuipc_wrapper;
import com.pakonat.util.PropertiesReader;

public class FSUI {
	
	private FSFlightSim sim;
	private FSUIPC fsui;
	private int ret = 0;

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


}
