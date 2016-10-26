package ephec.nsjc.httpserver.protocol;

import java.util.ArrayList;

import org.omg.CORBA.portable.ResponseHandler;

import ephec.nsjc.httpserver.connection.RequestHandler;

public class Request {

	private String method;
	private String requestURI;
	private String httpVersion;
	private ArrayList<Header> headers;
	private String body;
	
	public Request(){
		this.headers = new ArrayList<Header>();
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}
	public String getHttpVersion() {
		return httpVersion;
	}
	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}
	public ArrayList<Header> getHeaders() {
		return headers;
	}
	public void addHeader(Header h) {
		this.headers.add(h);
	}

	@Override
	public String toString() {
		return "Request [method=" + method + ", requestURI=" + requestURI + ", httpVersion=" + httpVersion
				+ ", headers=" + headers + "]";
	}

	public boolean hasHeader(String string) {
		for(Header h:headers){
			if(h.getLabel().equals(string)){
				return true;
			}
		}
		return false;
	}

	public String getHeader(String string) {
		for(Header h:headers){
			if(h.getLabel().equals(string)){
				return h.getValue();
			}
		}
		return null;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBody(){
		return this.body;
	}
	
	
	
	
}
