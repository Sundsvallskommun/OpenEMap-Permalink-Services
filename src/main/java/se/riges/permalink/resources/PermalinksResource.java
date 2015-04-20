package se.riges.permalink.resources;

import static se.riges.permalink.jooq.Tables.PERMALINKS;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riges.permalink.jooq.tables.records.PermalinksRecord;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Path("/permalinks")
public class PermalinksResource {
	
	static Logger logger = LoggerFactory.getLogger("Permalinks");

	static DSLContext create;
	
	@Context
	ServletContext context;
	
	void init() throws IOException {
		if (create == null) {
			logger.info("Loading database configuration");
			Properties db = new Properties();
			try (InputStream input = context.getResourceAsStream("/WEB-INF/db.properties")) {
				db.load(input);
			} catch (IOException e) {
				logger.error("FATAL: Could not read db.properties", e);
				throw e;
			}

			logger.info("Initializing database connection");
			HikariConfig hikariConfig = new HikariConfig(db);
			HikariDataSource ds = new HikariDataSource(hikariConfig);
			create = DSL.using(ds, SQLDialect.MYSQL);
		}
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public int post(String data) throws IOException {
		init();
		
		PermalinksRecord r = create.insertInto(PERMALINKS, PERMALINKS.DATA)
		.values(data)
		.returning(PERMALINKS.ID)
		.fetchOne();
		
		int id = r.getValue(0, Integer.class);
		
		return id;
	}
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public String get(@PathParam("id") int id) throws IOException {
		init();
		Record1<String> record = create.select(PERMALINKS.DATA)
				.from(PERMALINKS)
				.where(PERMALINKS.ID.equal(id))
				.fetchOne();
		if (record == null) {
			throw new RuntimeException("Det finns ingen permalink med id " + id);
		} else {
			return record.getValue(0, String.class);
		}
	}
}