package se.riges.permalink;

import java.io.IOException;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/")
public class Application extends ResourceConfig {

	public Application() throws IOException {
		packages("se.riges.permalink.resources");
		register(GenericExceptionMapper.class);
		register(JacksonFeature.class);
	}
}
