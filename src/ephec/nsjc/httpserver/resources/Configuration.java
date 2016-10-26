package ephec.nsjc.httpserver.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Configuration<T> {
	
	private T config;
	private Yaml yaml;
	
	public Configuration(Class<T> confClass){
		Constructor constructor = new Constructor(confClass);
		this.yaml = new Yaml(constructor);
		this.config = null;
	}
	
	@SuppressWarnings("unchecked")
	public T load(File f) throws FileNotFoundException{
		if(this.config == null) this.config = (T) this.yaml.load(new InputStreamReader(new FileInputStream(f)));
		return this.config;
	}
	
	
	
}
