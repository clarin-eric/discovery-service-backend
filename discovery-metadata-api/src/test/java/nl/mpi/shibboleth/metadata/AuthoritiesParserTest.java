package nl.mpi.shibboleth.metadata;

import eu.clarin.discovery.federation.Authorities;
import eu.clarin.discovery.federation.AuthoritiesFileParser;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wilelb
 */
public class AuthoritiesParserTest {
    
    @Test
    public void testMetadata1Hidden() {
        String resourceName = "authorities.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        AuthoritiesFileParser parser = new AuthoritiesFileParser();
        Authorities map = parser.parse(resource);
        Assert.assertNotNull("Federation map " + resourceName + " should not be null.", map);
        Assert.assertEquals("Number of federations", 67, map.getFederations().size());
    }
}
