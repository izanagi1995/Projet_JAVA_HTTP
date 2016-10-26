package ephec.nsjc.httpserver.protocol;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import ephec.nsjc.httpserver.HTTPServer;

public class Response {
	
	private ResponseCode responseCode;
	
	private ArrayList<Header> headers;
	private byte[] body;
	
	
	public Response(ResponseCode responseCode, ArrayList<Header> headers, byte[] body) {
		this.responseCode = responseCode;
		this.headers = headers;
		this.body = body;
	}
	
	public Response(ResponseCode responseCode, byte[] body) {
		this(responseCode, new ArrayList<Header>(), body);
	}

	public Response(ResponseCode responseCode) {
		this(responseCode, new ArrayList<Header>(), null);
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	public ArrayList<Header> getHeaders() {
		return headers;
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
	public void addHeader(Header h) {
		this.headers.add(h);
	}
	public void delHeader(String label) {
		this.headers.remove(this.getHeader(label));
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(String body) {
		this.delHeader("Content-Length");
		this.addHeader(new Header("Content-Length", Integer.toString(body.length())));
		this.body = body.getBytes();
	}
	
	public void setBody(byte[] body){
		this.delHeader("Content-Length");
		this.addHeader(new Header("Content-Length", Integer.toString(body.length)));
		this.body = body;
	}
	
	public void send(Socket s) throws IOException{
		String response = HTTPServer.httpVersion+" "+responseCode.getCode()+" "+responseCode.getDescr()+"\r\n";
		for(Header h: headers){
			response += h.getLabel()+": "+h.getValue()+"\r\n";
		}
		response += "\r\n";
		byte[] resRaw = response.getBytes();
		byte[] body = this.getBody();
		byte[] full = new byte[resRaw.length + body.length];
		System.arraycopy(resRaw, 0, full, 0, resRaw.length);
		System.arraycopy(body, 0, full, resRaw.length, body.length);
		s.getOutputStream().write(full);
	}
	
	
	
	
}
