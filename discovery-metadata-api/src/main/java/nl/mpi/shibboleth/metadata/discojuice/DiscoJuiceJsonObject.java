package nl.mpi.shibboleth.metadata.discojuice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.geoip.model.GeoLookupResult;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.IDPSSoDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.Organization;
import nl.mpi.shibboleth.metadata.shibboleth.Organization.Value;
import nl.mpi.shibboleth.metadata.shibboleth.SingleSignOnService;
import nl.mpi.shibboleth.metadata.shibboleth.UIInfo;
import nl.mpi.shibboleth.metadata.source.MetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IDP metadata:
 *  https://wiki.shibboleth.net/confluence/display/SHIB2/MetadataForIdP
 * 
 * IDP MD UI extension:
 *  https://wiki.shibboleth.net/confluence/display/SHIB2/IdPMDUIRecommendations
 * 
 * @author wilelb
 */
@XmlRootElement
public class DiscoJuiceJsonObject {

    private final static Logger logger = LoggerFactory.getLogger(DiscoJuiceJsonObject.class);
    
    /**
     * Generate the discojuice compatible json equivalent of the shibboleth xml metadata.
     * 
     * @param descriptor Shibboleth entity descriptor containing all the IDP SSO Descriptors.
     * @param lookup GEO IP lookup 
     * @param source Source configuration to load possible (country or weight) overrides from.
     * @return 
     */
    public static DiscoJuiceJsonObject create(EntityDescriptor descriptor, GeoIpLookup lookup, MetadataSource source) {
        DiscoJuiceJsonObject djjo = new DiscoJuiceJsonObject();
        
        //Gather all property values
        IDPSSoDescriptor sso = descriptor.getIdpSsoDescriptors().get(0);
        SingleSignOnService ssos = sso.getSsos().get(0);
        Organization org = null;
        if(descriptor.getOrganizations() != null && descriptor.getOrganizations().size() > 0) {
            org = descriptor.getOrganizations().get(0);
        } else {
            logger.debug("Missing Organization section");
        }
        
        UIInfo uiInfo = null;
        if(sso.getExtensions() != null && sso.getExtensions().getInfo() != null) {
            uiInfo = sso.getExtensions().getInfo();
        } else {
            logger.debug("Missing UIInfo section");
        }

        String url = ssos.getLocation();
        djjo.setEntityID(descriptor.getEntityID());
        
        if(uiInfo != null) { //Try to get a displayname in the specified language
             for(UIInfo.Name name : uiInfo.getNames()) {                 
                djjo.addTitle(new Title(name.getLang(), name.getName()));
             }            
             //set logo
             if(uiInfo.getLogo() != null) {
                Icon icon = new Icon();
                icon.url = uiInfo.getLogo().url;
                icon.width = uiInfo.getLogo().width;
                icon.height = uiInfo.getLogo().height;
                djjo.setIcon(icon);
             }
        } else if(org != null) { //Fallback to the default displayname
            for(Value name : org.getDisplayNames()) {
                djjo.addTitle(new Title(name.lang, name.value));
            }
        } else {
            //invalidInfo++;
            logger.warn("No displayname available");
        }

        logger.debug("Found in SAML metadata:");
        logger.debug("\tsso url: {}", url);
        logger.debug("\tentityID: {}", djjo.getEntityID());
        logger.debug("\torganizationDisplayName: {}", djjo.getTitles().get(0).getValue());
        
        //override weights
        int weight = 0;
        if(source.getWeights() != null) {
            if(source.getWeights().containsKey(djjo.getEntityID())) {
                weight = source.getWeights().get(djjo.getEntityID());
                logger.debug("Overriding weight for "+djjo.getEntityID()+" with value="+weight+".");
            } else {
                logger.debug("No weight found for "+djjo.getEntityID()+".");
            }
        } else {
            logger.debug("No weight overrides configured.");
        }
        //override countries
        String countryCode = null;
        if(source.getCountries() != null) {
            if(source.getCountries().containsKey(djjo.getEntityID())) {
                countryCode = source.getCountries().get(djjo.getEntityID());
                logger.debug("Overriding country code for "+djjo.getEntityID()+" with value="+countryCode+".");
            } else {
                logger.debug("No country code found for "+djjo.getEntityID()+".");
            }
        } else {
            logger.debug("No country code overrides configured.");
        }        

        djjo.setWeight(weight);        
        djjo.setCountryCode(countryCode);
        
        //set geospatial information
        GeoLookupResult lookupResult = lookup.performLookupByUrl(url);
        //Skip output if the geoip lookup failed for any reason
        if(!Float.isNaN(lookupResult.getLatitude()) && !Float.isNaN(lookupResult.getLongitute())) {
            Geo geo = new Geo();
            geo.setLat(lookupResult.getLatitude());
            geo.setLon(lookupResult.getLongitute());
            djjo.setGeo(geo);
            if(countryCode == null) {
                djjo.setCountryCode(lookupResult.getCountryCode());
            } else {
                djjo.setCountryCode(countryCode.toUpperCase());
            }
        }

        return djjo;
    }
    
    private String entityID;
    private List<Title> titles = new ArrayList<>();
    private String countryCode;
    private Geo geo;
    private int weight = 0;
    private Icon icon;

    /**
     * @return the entityID
     */
    @XmlElement(name = "entityID")
    public String getEntityID() {
        return entityID;
    }

    /**
     * @param entityID the entityID to set
     */
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    /**
     * @return the countryCode
     */
    @XmlElement(name = "country")
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
     * @return the geo
     */
    @XmlElement(name = "geo")
    public Geo getGeo() {
        return geo;
    }

    /**
     * @param geo the geo to set
     */
    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    /**
     * @return the weight
     */
    @XmlElement(name = "weight")
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the titles
     */
    public List<Title> getTitles() {
        return titles;
    }

    /**
     * @param titles the titles to set
     */
    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }
    
    public void addTitle(Title t) {
        this.titles.add(t);
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public static class Geo {

        private float lat;
        private float lon;

        /**
         * @return the lat
         */
        public float getLat() {
            return lat;
        }

        /**
         * @param lat the lat to set
         */
        public void setLat(float lat) {
            this.lat = lat;
        }

        /**
         * @return the lon
         */
        public float getLon() {
            return lon;
        }

        /**
         * @param lon the lon to set
         */
        public void setLon(float lon) {
            this.lon = lon;
        }
    }
    
    public static class Title {
        private String language;
        private String value;

        public Title() {}
        
        public Title(String language, String value) {
            this.language = language;
            this.value = value;
        }
        
        /**
         * @return the language
         */
        public String getLanguage() {
            return language;
        }

        /**
         * @param language the language to set
         */
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
                
    }
    
    public static class Icon {
        public String url;
        public Integer width;
        public Integer height;
    }
}