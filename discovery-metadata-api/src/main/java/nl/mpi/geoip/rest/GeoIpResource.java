package nl.mpi.geoip.rest;

import nl.mpi.geoip.model.GeoLookupResult;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.shibboleth.metadata.rest.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
@Path("/lookup")
public class GeoIpResource {
    private static Logger logger = LoggerFactory.getLogger(GeoIpResource.class);

    @javax.ws.rs.core.Context
    private ServletContext ctxt;

    public GeoIpResource() {}

    @GET
    @Path("/ip/{ip}")
    @Produces(MediaType.APPLICATION_XML)
    public GeoLookupResult lookupByIp(@PathParam("ip") String ip ) {
        try {
            GeoIpLookup lookup = Configuration.loadLookup(ctxt);
            GeoLookupResult result = lookup.performLookup(ip);
            if(result == null) {
                throw new IllegalStateException("Lookup failed, result is null");
            }
            return result;
        } catch(IllegalStateException ex) {
            logger.error("", ex);
        } catch(IOException ex) {
            logger.error("Failed to load geoip database.", ex);
        }
        return GeoLookupResult.createInvalidLookup();
    }

    @GET
    @Path("/url/{url}")
    @Produces(MediaType.APPLICATION_XML)
    public GeoLookupResult lookupByUrl(@PathParam("url") String token ) {
        try {
            GeoIpLookup lookup = Configuration.loadLookup(ctxt);
            GeoLookupResult result = lookup.performLookupByUrl(token);
            if(result == null) {
                throw new IllegalStateException("Lookup failed, result is null");
            }
            return result;
        } catch(IllegalStateException ex) {
            logger.error("", ex);
        } catch(IOException ex) {
            logger.error("Failed to load geoip database.", ex);
        }
        return GeoLookupResult.createInvalidLookup();
    }

    
}
