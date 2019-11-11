package nl.mpi.shibboleth.metadata;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import nl.mpi.geoip.DatabaseDownloader;
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

    public final static String REPORTING_CONFIG = "REPORTING";
    
    public final static String DEFAULT_GEOLITE2_CITY_FILENAME = "GeoLiteCity.dat";
    
    private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

    //private final static String[] parameters = new String[] {PRIVATE_IP,PUBLIC_IP,GEO_IP_DATABASE};

    private static Map<String,String> configuration =null;

    public static Map<String,String> loadConfiguration(ServletContext ctxt) {
        if(configuration == null) {
            if(ctxt == null) {
                logger.error("Servlet context is null, cannot load configuration");
            } else {
                configuration = new HashMap<>();
                //for(String parameter : parameters) {
                Enumeration<String> params = ctxt.getInitParameterNames();
                while(params.hasMoreElements()) {
                    String parameter = params.nextElement();
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

    public static GeoIpLookup loadLookup(ServletContext ctxt) throws IOException, NullPointerException {
        Map<String, String> config = Configuration.loadConfiguration(ctxt);
        if(config == null) {
            throw new NullPointerException("Configuration is null");
        }
        
        String geo_ip_database = getGeoIpDatabaseFile();
        GeoIpLookup lookup = new GeoIpLookupImplApiV2(
                    geo_ip_database,
                    config.get(Configuration.PRIVATE_IP),
                    config.get(Configuration.PUBLIC_IP)
                );
        return lookup;
    }
    
    public static String getGeoIpDatabaseFile() {
        String tmp_dir = System.getProperty("java.io.tmpdir");
        if(!tmp_dir.endsWith("/")) {
            tmp_dir += "/";
        }
        String geo_ip_database = tmp_dir+DEFAULT_GEOLITE2_CITY_FILENAME;
        if(configuration != null && configuration.get(Configuration.GEO_IP_DATABASE) != null) {
            geo_ip_database = configuration.get(Configuration.GEO_IP_DATABASE);
        }
        return geo_ip_database;
    }
    
    public static String getReportingPropertiesFile(ServletContext ctxt) {
        Map<String, String> config = Configuration.loadConfiguration(ctxt);
        if(!configuration.containsKey(Configuration.REPORTING_CONFIG)) {
            return null;
        }
        return configuration.get(Configuration.REPORTING_CONFIG);
    }
}
