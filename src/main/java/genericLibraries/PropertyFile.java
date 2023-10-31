package genericLibraries;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {
	
	public String getData(String key) throws IOException {
		
		Properties p = new Properties();
		FileInputStream fis= new FileInputStream("./src/test/resources/data.properties");
		p.load(fis);
		return p.getProperty(key);
	}

}
