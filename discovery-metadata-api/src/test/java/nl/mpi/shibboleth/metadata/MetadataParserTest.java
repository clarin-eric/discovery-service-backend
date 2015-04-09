package nl.mpi.shibboleth.metadata;

import java.net.MalformedURLException;
import java.net.URL;
import nl.mpi.geoip.impl.GeoIpLookupImplApiV2;
import nl.mpi.shibboleth.metadata.shibboleth.ContactPerson;
import nl.mpi.shibboleth.metadata.shibboleth.EntitiesDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.IDPSSoDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.Organization;
import nl.mpi.shibboleth.metadata.shibboleth.SingleSignOnService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataParserTest {

    private static final Logger logger = LoggerFactory.getLogger(MetadataParserTest.class);
    private final MetadataParser parser = new MetadataParser();

    public MetadataParserTest() {
    }

    private void parse(URL resource) {
        EntitiesDescriptor descriptor = parser.parse(resource);

        if (descriptor == null) {
            logger.error("Failed to parse shibboleth metadata.");
            Assert.assertNotNull(descriptor);
        } else {
            Assert.assertNotNull(descriptor.getEntityDescriptor());
            Assert.assertTrue(descriptor.getEntityDescriptor().size() > 0);

            for (EntityDescriptor ed : descriptor.getEntityDescriptor()) {
                Assert.assertNotNull(ed.getEntityID());
                Assert.assertNotNull(ed.getOrganizations());
                Assert.assertNotNull(ed.getContacts());
                Assert.assertNotNull(ed.getIdpSsoDescriptors());

                for (IDPSSoDescriptor sso : ed.getIdpSsoDescriptors()) {
                    Assert.assertNotNull(sso.getSsos());
                    Assert.assertTrue(sso.getSsos().size() > 0);

                    for (SingleSignOnService ssos : sso.getSsos()) {
                        Assert.assertNotNull(ssos.getLocation());
                    }
                }

                for (Organization org : ed.getOrganizations()) {
                    logger.info(org.getNames().get(0) + ", " + org.getDisplayNames().get(0) + ", " + org.getUrl());
                }

                for (ContactPerson contact : ed.getContacts()) {
                    logger.info(contact.getContactType() + ", " + contact.getSurname() + ", " + contact.getEmailAddress());
                }
            }
        }
    }
    
    /**
     * Test with CLARIN homeless metadata
     */
    @Test
    public void testMetadata1() {
        String resourceName = "test-metadata-1.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        parse(resource);
    }

    /**
     * Test with 'old' surf metadata
     */
    @Test
    public void testMetadata2() {
        String resourceName = "test-metadata-2.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        parse(resource);
    }
    
    /**
     * Test with 'new' surf metadata
     */
    @Test
    public void testMetadata3() {
        String resourceName = "test-metadata-3.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        parse(resource);
    }
    
    @Test
    public void testGeoHint() throws MalformedURLException {
        URL resource = new URL("https://infra.clarin.eu/aai/prod_md_about_spf_idps.xml");
        parse(resource);
    }
    
    @Test
    public void testGeoIpLookup() {
        
    }
}
