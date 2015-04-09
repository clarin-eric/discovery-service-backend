package nl.mpi.geoip;

import java.net.URL;
import nl.mpi.geoip.model.GeoLookupResult;

/**
 *
 * @author wilelb
 */
public interface GeoIpLookup {
   
    /**
     * Given the supplied id, perform a lookup and return the geo location.
     * 
     * @param ip
     * @return
     * @throws IllegalStateException 
     */
    public GeoLookupResult performLookup(String ip) throws IllegalStateException;

    /**
     * Given the supplied url, translate into an ip, perform the lookup and return
     * the geo location.
     * 
     * @param url
     * @return 
     */
    public GeoLookupResult performLookupByUrl(String url);

    /**
     * Given the supplied url, translate into an ip, perform the lookup and return
     * the geo location.
     * 
     * @param url
     * @return 
     */
    public GeoLookupResult performLookupByUrl(URL url);
}