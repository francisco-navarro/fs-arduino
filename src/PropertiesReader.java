import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesReader {
	
	private final String FILENAME = "arduino.properties";
	
	private Properties prop;
	
	PropertiesReader() throws Exception {
		File f = new File(FILENAME);
		
		if (!f.exists()) {
			System.err.println("File not exists: "+ FILENAME);
			f.createNewFile();
		}
		
		prop = new Properties();
		InputStream input = new FileInputStream(f);
		
		prop.load(input);
		prop.keys();
		input.close();
	}
	
	public String get(String key) {
		return prop.getProperty(key);
	}

	public String getPortName() {
		return prop.getProperty("PORT_NAME");
	}

}
