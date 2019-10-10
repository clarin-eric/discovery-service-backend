package nl.mpi.shibboleth.metadata.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.geoip.impl.GeoIpLookupImplApiV1;
import nl.mpi.geoip.impl.GeoIpLookupImplApiV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class Configuration {

    public final static String PRIVATE_IP = "LOCAL-IP";
    public final static String PUBLIC_IP = "PUBLIC-IP";
    public final static String GEO_IP_DATABASE = "GEO-IP-DATABASE";
    public final static String METADATA_SOURCES = "METADATA-SOURCES";

    private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

    private final static String[] parameters = new String[] {PRIVATE_IP,PUBLIC_IP,GEO_IP_DATABASE};

    private static Map<String,String> configuration =null;

    public static Map<String,String> loadConfiguration(ServletContext ctxt) {
        if(configuration == null) {
            if(ctxt == null) {
                logger.error("Servlet context is null, cannot load configuration");
            } else {
                configuration = new HashMap<>();
                for(String parameter : parameters) {
                    String value = ctxt.getInitParameter(parameter);
                    if(value == null) {
                        logger.error("Parameter " + parameter + " not configured!");
                    } else {
                        configuration.put(parameter, value);
                    }
                }
            }
        } else {
            logger.debug("Configuration already loaded");
        }
        return configuration;
    }

    public static GeoIpLookup loadLookup(ServletContext ctxt) throws IOException {
        Map<String, String> config = Configuration.loadConfiguration(ctxt);
        GeoIpLookup lookup = new GeoIpLookupImplApiV2(
                    config.get(Configuration.GEO_IP_DATABASE),
                    config.get(Configuration.PRIVATE_IP),
                    config.get(Configuration.PUBLIC_IP)
                );
        return lookup;
    }
}
