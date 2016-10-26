package ephec.nsjc.httpserver.connection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import ephec.nsjc.httpserver.HTTPServer;
import ephec.nsjc.httpserver.protocol.Header;
import ephec.nsjc.httpserver.protocol.Request;
import ephec.nsjc.httpserver.protocol.Response;
import ephec.nsjc.httpserver.protocol.ResponseCode;
import ephec.nsjc.httpserver.resources.FileUtils;

public class RequestHandler {
	
	private static final Logger log = Logger.getLogger(RequestHandler.class);
	
	private Request request;

	public RequestHandler(Request req) {
		this.request = req;
	}
	
	public Response handleRequest(){
		Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    String now = dateFormat.format(calendar.getTime());
	    if(request.getRequestURI().equals("/"))
	    	request.setRequestURI("/index.html");
	    String resource = HTTPServer.getServer().getWorkDir()+"/"+HTTPServer.getServer().getConfiguration().htdocs+request.getRequestURI();
	    Response s = null;
	    if(FileUtils.exists(resource)){
	    	s = new Response(ResponseCode.OK);
	    	try {
	    		byte[] content = FileUtils.getFileContent(resource);
				s.setBody(content);
				s.addHeader(new Header("Content-Type", FileUtils.getFileMIME(resource)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				s = new Response(ResponseCode.INTERNAL_ERROR);
				s.setBody(e.getMessage());
				s.addHeader(new Header("Content-Type", "text/html"));
			}
	    }else{
	    	s = new Response(ResponseCode.NOT_FOUND);
	    	s.setBody(ResponseCode.NOT_FOUND.getDescr());
	    	s.addHeader(new Header("Content-Type", "text/html"));
	    }
		s.addHeader(new Header("Server", "JavaHTTP-NSJC/1.0"));
		s.addHeader(new Header("Date", now));
		return s;
	}
	
	

}
