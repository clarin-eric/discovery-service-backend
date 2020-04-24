package nl.mpi.shibboleth.metadata.rest;

import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

/**
 *
 * @author wilelb
 */
public abstract class AbstractRestTest extends JerseyTest {

    @Override
    protected Application configure() {
        Application app = new ResourceConfig(MetadataResource.class);
        
        return app;
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new RestTestContainerFactory();
    }
    
}
