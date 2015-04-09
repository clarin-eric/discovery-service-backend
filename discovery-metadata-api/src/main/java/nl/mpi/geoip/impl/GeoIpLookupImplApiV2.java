package nl.mpi.geoip.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import java.io.File;
import java.io.IOException;
import nl.mpi.geoip.model.GeoLookupResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class GeoIpLookupImplApiV2 extends AbstractGeoIpLookupImpl {

    private final static Logger logger = LoggerFactory.getLogger(GeoIpLookupImplApiV2.class);
    
    private final DatabaseReader reader;
    
    public GeoIpLookupImplApiV2(String geoIpDatabaseFile, String localIp, String publicIp) throws IOException {
        super(geoIpDatabaseFile, localIp, publicIp);
        
        File database = new File(geoIpDatabaseFile);
        reader = new DatabaseReader.Builder(database).build();
    }
    
    @Override
    public GeoLookupResult performLookup(String ipOrHostname) throws IllegalStateException {
        try {            
            CityResponse response = reader.city(ipToInetAddress(ipOrHostname));

            GeoLookupResult result = new GeoLookupResult();
            result.setLatitude(response.getLocation().getLatitude().floatValue());
            result.setLongitute(response.getLocation().getLongitude().floatValue());
            result.setCountryName(response.getCountry().getName());
            result.setCountryCode(response.getCountry().getIsoCode());
            return result;
        } catch(IOException ex) {
            throw new IllegalStateException(ex);
        } catch(GeoIp2Exception ex) {
            throw new IllegalStateException(ex);
        }
    }    
}
