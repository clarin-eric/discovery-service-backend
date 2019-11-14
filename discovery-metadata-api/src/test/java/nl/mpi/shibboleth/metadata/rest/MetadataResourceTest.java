package nl.mpi.shibboleth.metadata.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import nl.mpi.shibboleth.metadata.source.MetadataSource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * Test the metadata conversion REST enpoint. Given an input configuration a 
 * specific conversion is run and is expected to generate valid JSON output.
 * 
 * @author wilelb
 */
public class MetadataResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(MetadataResource.class);
    }

    @Test
    public void testSimpleConversion() {
        List<String> sources = new ArrayList<>();
        sources.add(getClass().getClassLoader().getResource("test-metadata-1.xml").toString());
        Map<String, Integer> overrides = new HashMap<>();
        overrides.put("https://idp.clarin.eu", 200);
        Map<String, String> countries = new HashMap<>(); //country override
        countries.put("https://idp.clarin.eu", "EU");
        
        MetadataSource input = new MetadataSource();
        input.setCountries(countries);
        //input.setFederationMapSource(getClass().getClassLoader().getResource("authorities.xml").toString());        
        input.setFederationMapSource("https://raw.githubusercontent.com/clarin-eric/pyFF_config/master/authorities_to_country/authorities.xml");
        input.setMetadataSources(sources);
        input.setWeights(overrides);
        
                
        Entity<MetadataSource> inputXml = Entity.entity(input, MediaType.APPLICATION_XML_TYPE);
        final String response = target("metadata/discojuice").request().post(inputXml, String.class);
        final String expected = "[{\"country\":\"EU\",\"entityID\":\"https://idp.clarin.eu\",\"titles\":[{\"language\":\"en\",\"value\":\"Clarin.eu website account\"}],\"weight\":200}]";
        assertEquals(expected, response);
    }
    
    @Test
    public void testComplexConversion() {
        List<String> sources = new ArrayList<>();
        sources.add(getClass().getClassLoader().getResource("test-metadata-2.xml").toString());
        Map<String, Integer> overrides = new HashMap<>();
        overrides.put("https://idp.clarin.eu", 200);
        Map<String, String> countries = new HashMap<>(); //country override
        countries.put("https://idp.clarin.eu", "EU");
        
        MetadataSource input = new MetadataSource();
        input.setCountries(countries);
        input.setFederationMapSource(getClass().getClassLoader().getResource("authorities.xml").toString());        
        input.setMetadataSources(sources);
        input.setWeights(overrides);
        
                
        Entity<MetadataSource> inputXml = Entity.entity(input, MediaType.APPLICATION_XML_TYPE);
        final String response = target("metadata/discojuice").request().post(inputXml, String.class);
        assertNotNull(response);
        //TODO: properly validate the big output...
    }
}
