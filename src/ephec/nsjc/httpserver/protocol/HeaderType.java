package ephec.nsjc.httpserver.protocol;

import java.util.Arrays;

public enum HeaderType {
	GENERAL ("Cache-Control", "Connection", "Date", "Pragma", "Trailer", "Transfer-Encoding", "Upgrade", "Via", "Warning"),
	REQUEST ("Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language", "Authorization", "Expect", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range", "If-Unmodified-Since", "Max-Forwards", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent"),
	ENTITY  ("Allow", "Content-Encoding", "Content-Language", "Content-Length", "Content-Location", "Content-MD5", "Content-Range", "Content-Type", "Expires", "Last-Modified"),
	OTHER  ("");
	
	private String[] labels;

	HeaderType(String... labels){
		this.labels = labels;
	}
	
	public static HeaderType getType(String label){
		for(HeaderType t : HeaderType.values()){
			if(Arrays.asList(t.labels).contains(label)){
				return t;
			}
		}
		return HeaderType.OTHER;
	}
}
