package nl.mpi.shibboleth.metadata.source;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Define a set of metadata sources (url's) to be aggregated into a json metadata
 * file. Specific weights can be attached to specific entity ids.
 *
 * @author wilelb
 */
@XmlRootElement
public class MetadataSource {

    /**
     * @return the federationMapSource
     */
    public String getFederationMapSource() {
        return federationMapSource;
    }

    /**
     * @param federationMapSource the federationMapSource to set
     */
    public void setFederationMapSource(String federationMapSource) {
        this.federationMapSource = federationMapSource;
    }

    private String federationMapSource;
    
    /**
     * List of shibboleth metadata sources
     */
    private List<String> metadataSources;
    /**
     * Map of weights with entiry ids as keys
     */
    private Map<String, Integer> weights;

    /**
     * Map of country code overrides
     */
    private Map<String, String> countries;
    
    /**
     * @return the metadataSources
     */
    public List<String> getMetadataSources() {
        return metadataSources;
    }

    /**
     * @param metadataSources the metadataSources to set
     */
    public void setMetadataSources(List<String> metadataSources) {
        this.metadataSources = metadataSources;
    }

    /**
     * @return the weights
     */
    public Map<String, Integer> getWeights() {
        return weights;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(Map<String, Integer> weights) {
        this.weights = weights;
    }

    /**
     * @return the countries
     */
    public Map<String, String> getCountries() {
        return countries;
    }

    /**
     * @param countries the countries to set
     */
    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }
}
