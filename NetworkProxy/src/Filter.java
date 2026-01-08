import java.io.*;

public class Filter {
	private static final String fileName = "config/blocked_domains.txt";
	static boolean isGood(String target){
		try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
			String line;
			while ((line = in.readLine()) != null && !line.isEmpty()){
				if (target.contains(line)) return false;
			}
		}catch (FileNotFoundException e){
			System.out.println("No file found!!!");
		}catch (IOException e){
			System.out.println("IO Exception!!!");
		}
		return true;
	}
}
