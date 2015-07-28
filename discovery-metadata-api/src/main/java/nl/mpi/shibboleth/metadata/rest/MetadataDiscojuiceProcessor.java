package nl.mpi.shibboleth.metadata.rest;

import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.shibboleth.metadata.discojuice.DiscoJuiceJson;
import nl.mpi.shibboleth.metadata.discojuice.DiscoJuiceJsonObject;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.EntityExtensions.EntityAttribute;
import nl.mpi.shibboleth.metadata.source.MetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class MetadataDiscojuiceProcessor implements MetadataProcessor {

    private final static Logger logger = LoggerFactory.getLogger(MetadataDiscojuiceProcessor.class);
    
    private final DiscoJuiceJson discoJuiceJson = new DiscoJuiceJson();
    
    private final GeoIpLookup lookup;
    private long idpCount = 0;
    
    public MetadataDiscojuiceProcessor(GeoIpLookup lookup) {
        this.lookup = lookup;
    }
    
    @Override
    public void process(EntityDescriptor descriptor, MetadataSource source) {
        if (descriptor.isIDPDescriptor()) {
            try {
                //Check if this idp is indication to not be included in the DS
                boolean skip = false;
                if(descriptor.extensions != null && descriptor.extensions.attributes != null) {
                    for(EntityAttribute attr :  descriptor.extensions.attributes) {
                        if(attr.name.equalsIgnoreCase("http://macedir.org/entity-category") &&
                           attr.value.equalsIgnoreCase("http://refeds.org/category/hide-from-discovery")) {
                            skip = true;
                        }
                    }
                }
                
                if(!skip) {
                    DiscoJuiceJsonObject jsonObj
                        = DiscoJuiceJsonObject.create(descriptor, lookup, source);
                    discoJuiceJson.addObject(jsonObj);
                    idpCount++;
                } else {
                    logger.info("IDP [{}] skipped because of 'hide-from-discovery' flag.", descriptor.getEntityID());
                }
            } catch (NullPointerException ex) {
                logger.error("Failed to generate json for entityId: [" + descriptor.getEntityID() + "]");
                logger.error("", ex);
            } catch (Exception ex) {
                logger.error("Failed to generate json for entityId: [" + descriptor.getEntityID() + "]", ex);
            }

        }
    }

    /**
     * @return the discoJuiceJson
     */
    public DiscoJuiceJson getDiscoJuiceJson() {
        return discoJuiceJson;
    }

    /**
     * @return the idpCount
     */
    @Override
    public long getIdpCount() {
        return idpCount;
    }
}
