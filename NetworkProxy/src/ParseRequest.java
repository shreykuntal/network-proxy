import java.util.*;

public class ParseRequest {
	final Map<String, String> headers;
	ParseRequest(String data){
		headers = new HashMap<String, String>();
		headers.put("host", "");
		headers.put("host_port", "80");
		String[] split = data.split("\\r\\n");
		for (int i = 0; i < split.length; i++){
			String[] line = split[i].split(" ");
			if (i == 0){
				headers.put("method", line[0]);
				headers.put("request_target", line[1]);
			}else if (headers.containsKey(line[0].split(":")[0].toLowerCase())){
				headers.put(line[0].split(":")[0].toLowerCase(), line[1]);
			}
		}
		String[] hostPort = headers.get("host").split(":");
		if (hostPort.length > 1){
			headers.put("host_port", hostPort[1]);
		}
	}
	String getHost(){
		return headers.get("host");
	}
	String getMethod(){
		return headers.get("method");
	}
	String getRequestTarget(){
		return headers.get("request_target");
	}
	String getHostPort(){
		return headers.get("host_port");
	}
}
