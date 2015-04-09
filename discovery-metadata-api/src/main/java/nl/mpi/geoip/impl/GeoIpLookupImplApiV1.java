package nl.mpi.geoip.impl;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import java.io.IOException;
import nl.mpi.geoip.model.GeoLookupResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class GeoIpLookupImplApiV1 extends AbstractGeoIpLookupImpl {
    
    private final static Logger logger = LoggerFactory.getLogger(GeoIpLookupImplApiV1.class);

    private LookupService ls = null;

    public GeoIpLookupImplApiV1(String geoIpDatabaseFile, String localIp, String publicIp) throws IOException {
        super(geoIpDatabaseFile, localIp, publicIp);

        try {
            ls = new LookupService(this.geoIpDatabaseFile, LookupService.GEOIP_MEMORY_CACHE );
            logger.info("Loaded geo IP database");
            logger.info("\tPath:{}", geoIpDatabaseFile);
            logger.info("\tInfo: {}", ls.getDatabaseInfo().toString());
            logger.info("\tLocal ip transformation: {} --> {}", localIp, publicIp);
        } catch(IOException ex) {
            throw ex;
        }
    }

    @Override
    public GeoLookupResult performLookup(String ip) throws IllegalStateException {
        if(ls != null) {
            Location location = ls.getLocation(ipToInetAddress(ip));
            //throw an exception if the lookup failed
            if(location == null) {
                throw new IllegalStateException("Failed to lookup ip: ["+ip+"]");
            } else {
                logger.debug("Found location {} for ip {}", location.toString(), ip);
            }
            return GeoLookupResult.createLookupFromLocation(location);
        }
        throw new IllegalStateException("Geo location database has not been loaded");
    }
}