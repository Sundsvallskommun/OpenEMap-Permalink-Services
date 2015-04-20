package se.riges.permalink.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/")
public class Version {
	
	@Context
	ServletContext servletContext;
	
	@GET
	public String get() throws IOException  {
		InputStream inputStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
		Manifest manifest = new Manifest(inputStream);
		String title = manifest.getMainAttributes().getValue("Implementation-Title");
		String version = manifest.getMainAttributes().getValue("Implementation-Version");
		return title + " " + version;
	}
}