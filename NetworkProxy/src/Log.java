import java.io.*;
import java.util.logging.*;

public class Log {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final static int limit = 1024 * 1024;
	private final static int count = 5;
	Log(String fileName){
		try {
			Handler fh = new FileHandler(fileName, limit, count, true);
			SimpleFormatter simple = new SimpleFormatter();
			fh.setFormatter(simple);
			LOGGER.addHandler(fh);
		}catch(IOException e){
			System.out.println("IOException !!!");
		}
	}
	void put(String message) {
		LOGGER.log(Level.INFO, message);
	}
}
