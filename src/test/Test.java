package test;
import com.flightsim.fsuipc.*;


public class Test
{
	static void TestADF()
	{
	System.out.println("Testing ADF");
	FSADF adf = new FSADF();
	System.out.println("freq " + adf.Freq());
	System.out.println("freq as string " + adf.FreqAsString());
	System.out.println("ID " +adf.Identity());
	System.out.println("Name " + adf.Name());
	}

	static void TestNav1()
	{
	System.out.println("TestNav1");
	FSNav1 nav = new FSNav1();
	System.out.println("freq " + nav.Freq());
	System.out.println("freq as string " + nav.FreqAsString());
	System.out.println("ID " +nav.Identity());
	System.out.println("Name " + nav.Name());
	System.out.println("loc " +nav.LocaliserNeedle());
	System.out.println("glide " + nav.GlideSlope());
	}
	
	static void TestAircraft()
	{
	System.out.println("TestAircraft");
	FSAircraft air = new FSAircraft();
	System.out.println("latitude " + air.Latitude());
	System.out.println("longtitude " + air.Longitude());
	System.out.println("VOR1 lat " + air.VOR1LocLatitude());
	System.out.println("VOR1 long " + air.VOR1LocLongitude());
	
	int hi = air.Heading();
	System.out.println("heading " + hi);
	double h = 360.0*hi/(65536.0*65536.0);
	System.out.println("h " + h);
	
	int mi = air.Magnetic();
	System.out.println("magnetic " + mi);
	double m = 360.0*mi/(65536.0*65536.0);
	System.out.println("m " + m);
	
	double p = air.Pitch();
	System.out.println("pitch " + p);
	
	double b = air.Bank();
	System.out.println("bank " + b);
	

	System.out.println("IAS " + air.IAS()/128.0);
	System.out.println("VS " + air.VerticalSpeed());
	System.out.println("alt " + air.Altitude());
	System.out.println("locerr " + air.LocaliserError());
	System.out.println("loc " + air.Localiser());
	System.out.println("engine " + air.NumberOfEngines());
	System.out.println("engine type " +air.EngineType());
	
	FSUIPC fsui = new FSUIPC();
	
	System.out.println("RPM " + (fsui.getLong(0x0898)/ 163.84) + "%");
	}
	
	static void TestEngine1()
	{
	System.out.println("TestEngine1");
	FSEngine1 eng = new FSEngine1();
	
	

	System.out.println("comb " + eng.Combustion());
	}

	static void TestEngine2()
	{
	System.out.println("TestEngine2");
	FSEngine2 eng = new FSEngine2();

	System.out.println("comb " + eng.Combustion());
	}
	
	static void TestGear()
	{
	System.out.println("TestGear");
	FSGear gear = new FSGear();
	System.out.println("nose" + gear.NoseGearState());
	System.out.println("left " + gear.LeftGearState());
	System.out.println("right " + gear.RightGearState());
	

	}

	static void TestFlightSim()
	{
	System.out.println("\nTestFlightSim");
	FSFlightSim sim = new FSFlightSim();
	System.out.println("name " + sim.StartSituationName());
	}
	
	public static void main(String s[]) 
	{
	System.out.println("Running tests");
	int ret = fsuipc_wrapper.Open(fsuipc_wrapper.SIM_ANY);
	System.out.println("ret =" + ret);
	if(ret == 0 )
		{
		System.out.println("Flight sim not found");
		}
	else
		{
		TestADF();
		TestNav1();
		TestAircraft();
		//TestEngine1();
		// TestEngine2();
		//TestGear();
		//TestFlightSim();
		}
	}
}