package ephec.nsjc.httpserver.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ephec.nsjc.httpserver.exceptions.ParserException;


public class RequestParser {
	
	private BufferedReader reader;
	
	public static final Pattern requestLinePattern = Pattern.compile("(.*?) (.*?) (.*)");
	
	public RequestParser(BufferedReader reader){
		this.reader = reader;
	}
	
	public Request parse() throws ParserException{
		Request r = new Request();
		try {
			String requestLine = this.reader.readLine();
			Matcher m = requestLinePattern.matcher(requestLine);
			if(m.find()){
				r.setMethod(m.group(1));
				r.setRequestURI(m.group(2));
				r.setHttpVersion(m.group(3));
			}else{
				throw new ParserException("Cannot read request line \""+requestLine+"\"");
			}
			
			String headerLine = null;
			while((headerLine = this.reader.readLine()).length() != 0){
				System.out.println("=> "+headerLine);
				r.addHeader(new Header(headerLine.split(":")[0], headerLine.split(":")[1]));
			}
			String body = null;
			boolean hasTEncoding = r.hasHeader("Transfer-Encoding");
			boolean isChunked = r.hasHeader("Transfer-Encoding")?r.getHeader("Transfer-Encoding").equals("chunked"):false;
			boolean hasContentLength = r.hasHeader("Content-Length");
			if(hasTEncoding && isChunked){
				
			}else if(hasTEncoding){
				
			}else if(hasContentLength){
				char[] buffer = new char[Integer.parseInt(r.getHeader("Content-Length"))];
				this.reader.read(buffer, 0, Integer.parseInt(r.getHeader("Content-Length")));
				body = new String(buffer);
			}
			r.setBody(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	
}
