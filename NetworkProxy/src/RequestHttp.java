import java.io.*;
import java.net.*;

public class RequestHttp {
	static final int timeOut = 5000;
	static void handleHttp(Socket socket, Socket server, ParseRequest parsed, BufferedReader in, PrintWriter out, StringBuffer input, Log logger) throws IOException{
		boolean isAllowed = Filter.isGood(parsed.getRequestTarget());
		try {
			server.connect(new InetSocketAddress(parsed.getHost(), Integer.parseInt(parsed.getHostPort())), timeOut);
		}catch (SocketTimeoutException err){
			System.out.println("Socket Timeout!!!");
		}catch (IOException err){
			System.out.println("Malformed URL!!!");
		}
		String status = (isAllowed && server.isConnected() ? "" : "HTTP/1.1 403 Forbidden");
		int size = 0, c;
		if (isAllowed && server.isConnected()) {
			server.getOutputStream().write(input.toString().getBytes());
			PrintWriter serverOut = new PrintWriter(server.getOutputStream());
			BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
			serverOut.write(input.toString());
			serverOut.flush();
			String line;
			while (!(line = serverIn.readLine()).isEmpty()) {
				out.write(line + "\r\n");
				if (status.isEmpty()) {
					status = line;
				}
				if (line.contains("Content-Length")) {
					size = Integer.parseInt(line.split(": ")[1]);
				}
			}
			out.write("\r\n");
			for (int i = 0; i < size; i++){
				c = serverIn.read();
				out.write(c);
			}
		}else{
			out.write(status+"\r\n");
		}
		String logMessage = "Client IP:Port  "+socket.getInetAddress()+":"+socket.getPort()+
				"\nRequested Host:Port  "+parsed.getHost()+":"+parsed.getHostPort()+
				"\nRequested Target  "+parsed.getRequestTarget()+
				"\nAction  "+(isAllowed ? "Allowed" : "Blocked")+
				"\nResponse Status  "+status+
				"\nSize Transferred  "+size+"\n";
		logger.put(logMessage);
	}
}
