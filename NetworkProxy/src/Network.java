import java.net.*;
import java.io.*;

public class Network {
	private int localPort;
	private String logFile;
	private Log logger;
	void runProxyServer(){
		try(BufferedReader fh = new BufferedReader(new InputStreamReader(new FileInputStream("config/server_config.txt")))){
			localPort = Integer.parseInt(fh.readLine());
			logFile = "logs/"+fh.readLine();
		}catch(FileNotFoundException err){
			System.out.println("config/server_config.txt NOT FOUND!!!");
			return;
		}catch(IOException err){
			System.out.println("IO Exception!!!");
			return;
		}
		logger = new Log(logFile);
		System.out.println("===Server listening at port "+localPort+"===");
		try(ServerSocket server = new ServerSocket(localPort)) {
			while (true) {
				Socket clientSocket = server.accept();
				new Thread(() -> handleClient(clientSocket)).start();
			}
		}catch (IOException err){
			System.out.println("IO exception!!!");
		}
	}
	void handleClient(Socket clientSocket){
		try(Socket socket = clientSocket;
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Socket server = new Socket();
			)
		{
			StringBuffer input = new StringBuffer();
			String line;
			while (!(line = in.readLine()).isEmpty()){
				input.append(line).append("\r\n");
			}
			input.append("\r\n");
			socket.getOutputStream().flush();
			ParseRequest parsed = new ParseRequest(input.toString());
			if (parsed.getMethod().equals("CONNECT")){
				RequestHttpS.handleHttpS(socket, server, parsed, input, logger);
			}else{
				RequestHttp.handleHttp(socket, server, parsed, in, out, input, logger);
			}
		}catch (IOException err){
			System.out.println("IOException hhehe!!!");
		}
	}
}
