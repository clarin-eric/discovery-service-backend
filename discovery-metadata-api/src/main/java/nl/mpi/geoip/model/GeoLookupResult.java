package nl.mpi.geoip.model;

import com.maxmind.geoip.Location;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilelb
 */
@XmlRootElement
public class GeoLookupResult {

    public static GeoLookupResult createInvalidLookup() {
        GeoLookupResult result = new GeoLookupResult();
        result.setCountryCode("");
        result.setCountryName("Unkown");
        result.setLatitude(Float.NaN);
        result.setLongitute(Float.NaN);
        return result;
    }

    public static GeoLookupResult createLookupFromLocation(Location location) {
        if(location != null) {
            GeoLookupResult result = new GeoLookupResult();
            result.setCountryCode(location.countryCode);
            result.setCountryName(location.countryName);
            result.setLatitude(location.latitude);
            result.setLongitute(location.longitude);
            return result;
        }
        return createInvalidLookup();
    }

    private String countryName;
    private String countryCode;

    private float latitude;
    private float longitute;

    /**
     * @return the countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName the countryName to set
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitute
     */
    public float getLongitute() {
        return longitute;
    }

    /**
     * @param longitute the longitute to set
     */
    public void setLongitute(float longitute) {
        this.longitute = longitute;
    }
}
