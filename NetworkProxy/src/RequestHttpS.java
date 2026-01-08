import java.io.*;
import java.net.*;

public class RequestHttpS {
	static final int timeOut = 5000;
	static void handleHttpS(Socket socket, Socket server, ParseRequest parsed, StringBuffer input, Log logger) throws IOException {
		boolean isAllowed = Filter.isGood(parsed.getRequestTarget());
		try {
			server.connect(new InetSocketAddress(parsed.getHost().split(":")[0], Integer.parseInt(parsed.getHostPort())), timeOut);
		}catch (SocketTimeoutException err){
			System.out.println("Socket Timeout!!!");
		}catch (IOException err){
			System.out.println("Malformed URL!!!");
		}
		String status = (isAllowed && server.isConnected() ? "HTTP/1.1 200 Connection Established" : "HTTP/1.1 403 Forbidden");
		InputStream cin = socket.getInputStream();
		OutputStream cout = socket.getOutputStream();
		cout.write((status+"\r\n\r\n").getBytes());
		cout.flush();
		String logMessage = "Client IP:Port  "+socket.getInetAddress()+":"+socket.getPort()+
				"\nRequested Host:Port  "+parsed.getHost().split(":")[0]+":"+parsed.getHostPort()+
				"\nRequested Target  "+parsed.getRequestTarget()+
				"\nAction  "+(isAllowed ? "Allowed" : "Blocked")+
				"\nResponse Status  "+status+"\n";
		logger.put(logMessage);
		if (isAllowed && server.isConnected()){
			InputStream sin = server.getInputStream();
			OutputStream sout = server.getOutputStream();
			Thread t1 = new Thread(() -> pipe(sin, cout));
			Thread t2 = new Thread(() -> pipe(cin, sout));
			t1.start();
			t2.start();
			try{
				t1.join();
				t2.join();
			} catch (InterruptedException e){
				System.out.println("Interrrrupt!!!");
			}
		}
	}
	static void pipe(InputStream in, OutputStream out){
		try {
			byte[] buf = new byte[8192];
			int c;
			while ((c = in.read(buf)) != -1) {
				out.write(buf, 0, c);
				out.flush();
			}
		} catch (IOException e){
			System.out.println("IO exception !!!");
		}
	}
}
