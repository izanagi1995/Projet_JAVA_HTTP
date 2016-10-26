package ephec.nsjc.httpserver.resources;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
	public static String getFileMIME(String path) throws IOException{
		return URLConnection.guessContentTypeFromName(new File(path).getName());
	}
	
	public static byte[] getFileContent(String path) throws IOException{
		return Files.readAllBytes(Paths.get(path));
	}
	
	public static long getSize(String path){
		return new File(path).length();
	}
	
	public static boolean exists(String path){
		return new File(path).exists();
	}
}
