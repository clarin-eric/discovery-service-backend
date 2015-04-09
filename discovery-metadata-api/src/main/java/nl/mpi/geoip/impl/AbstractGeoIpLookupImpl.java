package nl.mpi.geoip.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import nl.mpi.geoip.DatabaseDownloader;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.geoip.model.GeoLookupResult;

/**
 *
 * @author wilelb
 */
public abstract class AbstractGeoIpLookupImpl implements GeoIpLookup {
    
    protected final String geoIpDatabaseFile;
    protected final String localIp;
    protected final String publicIp;
    
    public AbstractGeoIpLookupImpl(String geoIpDatabaseFile, String localIp, String publicIp) throws IOException {
        this.geoIpDatabaseFile = geoIpDatabaseFile;
        this.localIp = localIp;
        this.publicIp = publicIp;
    }
    
    public void initialize() {
        DatabaseDownloader downloader = new DatabaseDownloader(this.geoIpDatabaseFile);
        downloader.checkForUpdate();
    }
    
    /**
     * Convert a hostname or ip address into an java InetAdress.
     * 1. If a hostname is given, a dns lookup is performed to fetch that hosts
     * ip address. 
     * 2. If an ip address is given, it's only validated.
     * 
     * @param ip
     * @return 
     */
    protected InetAddress ipToInetAddress(String ip) {
        //bypass our local network by using our external ip
        if(localIp != null && !localIp.isEmpty() && ip.startsWith(localIp)) {
            ip = publicIp;
        }
        
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            //return null;
        }
        return addr;
    }
    
    @Override
    public abstract GeoLookupResult performLookup(String ipOrHostname) throws IllegalStateException;

    @Override
    public GeoLookupResult performLookupByUrl(String url) {
        try {
            return performLookupByUrl(new URL(url));
        } catch(MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public GeoLookupResult performLookupByUrl(URL url) {
        return performLookup(url.getHost());
    }
}
