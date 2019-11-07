package nl.mpi.shibboleth.metadata.discojuice;

import eu.clarin.discovery.federation.Authorities;
import eu.clarin.url.TldUtils;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.geoip.model.GeoLookupResult;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.IDPSSoDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.Organization;
import nl.mpi.shibboleth.metadata.shibboleth.SingleSignOnService;
import nl.mpi.shibboleth.metadata.shibboleth.UIInfo;
import nl.mpi.shibboleth.metadata.source.MetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class DiscoJuideJsonObjectBuilder {

    private final static Logger logger = LoggerFactory.getLogger(DiscoJuideJsonObjectBuilder.class);

    public static DiscoJuiceJsonObject create(EntityDescriptor descriptor, Authorities map, MetadataSource source, GeoIpLookup lookup) {
        DiscoJuideJsonObjectBuilder builder = new DiscoJuideJsonObjectBuilder();
        String entityID = descriptor.getEntityID();
        logger.debug("Processing: {}", entityID);
        
        builder.setEntityId(entityID);
        builder.setWeight(entityID, source);
        builder.setIcon(descriptor);
        builder.setCountryCode(descriptor, source, map, lookup);
        builder.setTitle(descriptor);
        return builder.build();
    }

    private final DiscoJuiceJsonObject djjo;

    private DiscoJuideJsonObjectBuilder() {
        this.djjo = new DiscoJuiceJsonObject();
    }

    private DiscoJuiceJsonObject build() {
        return this.djjo;
    }
    
    private static String getRegistrationAuthority(EntityDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }
        if (descriptor.getExtensions() == null) {
            return null;
        }
        if (descriptor.getExtensions().registrationInfo == null) {
            return null;
        }
        if (descriptor.getExtensions().registrationInfo.registrationAuthority == null) {
            return null;
        }
        return descriptor.getExtensions().registrationInfo.registrationAuthority;
    }

    private static String getCountryForRegistrationAuthority(String registrationAuthority, Authorities map) {
        if(map == null) {
            return null;
        }
        
        String countryCode = null;
        if (registrationAuthority != null) {
            Authorities.Authority fed = map.getAuthority(registrationAuthority);
            if (fed != null) {                
                countryCode = fed.getCode();
                logger.debug("Mapped registration authority={} to country={}", registrationAuthority, countryCode);
            } else {
                logger.warn("No country mapping found for registration authority={}", registrationAuthority);
            }
        }
        return countryCode;
    }

    private static String getOverrideCountryCode(MetadataSource source, String entityID) {
        String countryCode = null;
        if (source.getCountries() != null) {
            if (source.getCountries().containsKey(entityID)) {
                countryCode = source.getCountries().get(entityID);
                logger.debug("Overriding country code for " + entityID + " with value=" + countryCode + ".");
            } else {
                logger.debug("No country code override found for " + entityID + ".");
            }
        } else {
            logger.debug("No country code overrides configured.");
        }
        return countryCode;
    }

    private static String getGeoIpCountryCode(GeoIpLookup lookup, String singleSignOnServiceUrl) {
        if(lookup == null) {
            return null;
        }
        
        String countryCode = null;
        try {
            GeoLookupResult lookupResult = lookup.performLookupByUrl(singleSignOnServiceUrl);
            if (!Float.isNaN(lookupResult.getLatitude()) && !Float.isNaN(lookupResult.getLongitute())) {
                countryCode = lookupResult.getCountryCode();
                logger.debug("Looked up country={} by geo ip location", countryCode);
            } else {
                logger.warn("No geo ip lookup for {}", singleSignOnServiceUrl);
            }
        } catch (IllegalStateException ex) {
            logger.error("IllegalStateException while looking up entity in geoip database. Error: {}", ex.getMessage());
            logger.debug("Stacktrace:", ex);
        } catch (NullPointerException ex) {
            logger.error("NullPointerException while looking up entity in geoip database.");
            logger.debug("Stacktrace:", ex);
        }
        return countryCode;
    }

    private static String getTldCountryCode(String singleSignOnServiceUrl) {
        return TldUtils.getTldCountryCode(singleSignOnServiceUrl);
    }

    /**
     * Try a number of strategies to get a country code for a specific entity.
     * If none of the strategies match, no country code is set.
     * 
     * @param descriptor
     * @param source
     * @param map
     * @param lookup
     * @return 
     */
    public DiscoJuideJsonObjectBuilder setCountryCode(EntityDescriptor descriptor, MetadataSource source, Authorities map, GeoIpLookup lookup) {
        String entityID = descriptor.getEntityID();       
        IDPSSoDescriptor sso = descriptor.getIdpSsoDescriptors().get(0);
        SingleSignOnService ssos = sso.getSsos().get(0);
        String singleSignOnServiceUrl = ssos.getLocation();
        String registrationAuthority = getRegistrationAuthority(descriptor);

        String countryCode = getOverrideCountryCode(source, entityID);
        if (countryCode != null) {
            this.djjo.setCountry(countryCode);
            logger.debug("Set country code to {} based on override", countryCode);
            return this;
        }

        countryCode = getCountryForRegistrationAuthority(registrationAuthority, map);
        if (countryCode != null) {
            this.djjo.setCountry(countryCode);
            logger.debug("Set country code to {} based on registration authority", countryCode);
            return this;
        }

        countryCode = getGeoIpCountryCode(lookup, singleSignOnServiceUrl);
        if (countryCode != null) {
            this.djjo.setCountry(countryCode);
            logger.debug("Set country code to {} based on geo ip lookup", countryCode);
            return this;
        }

        countryCode = getTldCountryCode(singleSignOnServiceUrl);
        if (countryCode != null) {
            this.djjo.setCountry(countryCode);
            logger.debug("Set country code to {} based on single sign on url tld", countryCode);
            return this;
        }
        
        logger.warn("No country code set");

        return this;
    }

    public DiscoJuideJsonObjectBuilder setWeight(String entityID, MetadataSource source) {
        int weight = 0;
        if (source.getWeights() != null) {
            if (source.getWeights().containsKey(entityID)) {
                this.djjo.setWeight(source.getWeights().get(entityID));
                logger.debug("Overriding weight for " + entityID + " with value=" + weight + ".");
            } else {
                logger.debug("No weight found for " + entityID + ".");
            }
        } else {
            logger.debug("No weight overrides configured.");
        }
        return this;
    }

    public DiscoJuideJsonObjectBuilder setIcon(EntityDescriptor descriptor) {
        IDPSSoDescriptor sso = descriptor.getIdpSsoDescriptors().get(0);
        UIInfo uiInfo = null;
        if (sso.getExtensions() != null && sso.getExtensions().getInfo() != null) {
            uiInfo = sso.getExtensions().getInfo();
            if(uiInfo.getLogo() != null && !uiInfo.getLogo().url.startsWith("data:")) {
                DiscoJuiceJsonObject.Icon icon = new DiscoJuiceJsonObject.Icon();
                icon.url = uiInfo.getLogo().url;
                icon.width = uiInfo.getLogo().width;
                icon.height = uiInfo.getLogo().height;
                djjo.setIcon(icon);
             }
        } else {
            logger.debug("Missing UIInfo section");
        }
        
        return this;
    }

    public DiscoJuideJsonObjectBuilder setEntityId(String entityID) {
        djjo.setEntityID(entityID);
        return this;
    }

    public DiscoJuideJsonObjectBuilder setTitle(EntityDescriptor descriptor) {
        Organization org = null;
        if (descriptor.getOrganizations() != null && descriptor.getOrganizations().size() > 0) {
            org = descriptor.getOrganizations().get(0);
        } else {
            logger.debug("Missing Organization section");
        }

        IDPSSoDescriptor sso = descriptor.getIdpSsoDescriptors().get(0);
        UIInfo uiInfo = null;
        if (sso.getExtensions() != null && sso.getExtensions().getInfo() != null) {
            uiInfo = sso.getExtensions().getInfo();
        } else {
            logger.debug("Missing UIInfo section");
        }

        if (uiInfo != null) { //Try to get a displayname in the specified language
            for (UIInfo.Name name : uiInfo.getNames()) {
                djjo.addTitle(new DiscoJuiceJsonObject.Title(name.getLang(), name.getName()));
            }
        } else if (org != null) { //Fallback to the default displayname
            for (Organization.Value name : org.getDisplayNames()) {
                djjo.addTitle(new DiscoJuiceJsonObject.Title(name.lang, name.value));
            }
        } else {
            logger.warn("No displayname available");
        }
        return this;
    }
}
