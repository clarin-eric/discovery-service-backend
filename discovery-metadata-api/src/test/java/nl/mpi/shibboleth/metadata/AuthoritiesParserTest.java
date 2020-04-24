package nl.mpi.shibboleth.metadata;

import eu.clarin.discovery.federation.Authorities;
import eu.clarin.discovery.federation.AuthoritiesFileParser;
import eu.clarin.discovery.federation.FileBackedAuthoritiesFileParser;
import java.io.IOException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author wilelb
 */
public class AuthoritiesParserTest {
    
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();
    
    @Test
    public void testMetadata1Hidden() throws IOException {        
        String resourceName = "authorities.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        String localFile = folder.newFile(resourceName).getAbsolutePath();
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        AuthoritiesFileParser parser = new FileBackedAuthoritiesFileParser(localFile);
        Authorities map = parser.parse(resource);
        Assert.assertNotNull("Federation map " + resourceName + " should not be null.", map);
        Assert.assertEquals("Number of federations", 67, map.getAuthorities().size());
    }
}
