package nl.mpi.shibboleth.metadata;

import java.net.MalformedURLException;
import java.net.URL;
import nl.mpi.shibboleth.metadata.shibboleth.EntitiesDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.IDPSSoDescriptor;
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

    private EntitiesDescriptor parse(URL resource) {
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

                /*
                for (Organization org : ed.getOrganizations()) {
                    logger.info(org.getNames().get(0) + ", " + org.getDisplayNames().get(0) + ", " + org.getUrl());
                }

                for (ContactPerson contact : ed.getContacts()) {
                    logger.info(contact.getContactType() + ", " + contact.getSurname() + ", " + contact.getEmailAddress());
                }
                */
            }
        }
        
        return descriptor;
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
     * Test with CLARIN homeless metadata, hidden from discovery
     */
    @Test
    public void testMetadata1Hidden() {
        String resourceName = "test-metadata-1-hide.xml";
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
    public void testMetadata4() {
        String resourceName = "test-metadata-4.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        
        EntitiesDescriptor descriptors = parse(resource);
        Assert.assertNotNull(descriptors);
        Assert.assertNotNull(descriptors.getEntityDescriptor());
        Assert.assertTrue("Expected one entity descriptor", descriptors.getEntityDescriptor().size() == 1);
        EntityDescriptor descriptor = descriptors.getEntityDescriptor().get(0);
        Assert.assertNotNull("Extensions expected", descriptor.getExtensions());
        Assert.assertNotNull("Expected registration info in extensions.", descriptor.getExtensions().registrationInfo);
        Assert.assertNotNull(descriptor.getExtensions().attributes);
        Assert.assertTrue(descriptor.getExtensions().attributes.size() > 0);
        Assert.assertNotNull(descriptor.getExtensions().attributes.get(0));
        Assert.assertNotNull(descriptor.getExtensions().attributes.get(0).name);
        Assert.assertNotNull(descriptor.getExtensions().attributes.get(0).nameFormat);
        Assert.assertNotNull(descriptor.getExtensions().attributes.get(0).value);
    }
    
    @Test
    public void testMetadata5() {
        String resourceName = "test-metadata-encoding.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        EntitiesDescriptor descriptors = parse(resource);
        Assert.assertNotNull(descriptors);
        Assert.assertNotNull(descriptors.getEntityDescriptor());
        Assert.assertTrue("Expected one entity descriptor", descriptors.getEntityDescriptor().size() == 1);
        EntityDescriptor descriptor = descriptors.getEntityDescriptor().get(0);
        String name = descriptor.getIdpSsoDescriptors().get(0).getExtensions().getInfo().getNames().get(0).getName();
        logger.info("Name={}", name);
    }
    
    @Test
    public void testMetadata6() {
        String resourceName = "test-metadata-encoding2.xml";
        URL resource = this.getClass().getClassLoader().getResource(resourceName);
        Assert.assertNotNull("Resource " + resourceName + " should be provided in the test resources.", resource);
        EntitiesDescriptor descriptors = parse(resource);
        Assert.assertNotNull(descriptors);
        Assert.assertNotNull(descriptors.getEntityDescriptor());
        Assert.assertTrue("Expected one entity descriptor", descriptors.getEntityDescriptor().size() == 1);
        EntityDescriptor descriptor = descriptors.getEntityDescriptor().get(0);
        String name = descriptor.getIdpSsoDescriptors().get(0).getExtensions().getInfo().getNames().get(0).getName();
        logger.info("Name={}", name);
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
