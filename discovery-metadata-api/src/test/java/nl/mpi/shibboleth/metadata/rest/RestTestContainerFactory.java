package nl.mpi.shibboleth.metadata.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.glassfish.jersey.test.spi.TestHelper;

/**
 * Unify GrizzlyWebContainerFactory and GrizzlyHttpServerFactory so that both a
 * servlet context and resourceconfig is supported.
 *
 * Based on https://stackoverflow.com/a/42027506
 *
 * @author wilelb
 */
public class RestTestContainerFactory implements TestContainerFactory {

    public static class RestTestContainer implements TestContainer {

        private static final Logger LOGGER = Logger.getLogger(RestTestContainer.class.getName());

        private URI baseUri = null;
        private final HttpServer server;

        public RestTestContainer(final URI baseUri, final DeploymentContext context) {
            this.baseUri = UriBuilder.fromUri(baseUri).path(context.getContextPath()).build();
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Creating RestRestContainer configured at the base URI " + TestHelper.zeroPortToAvailablePort(baseUri));
            }

            try {
                final WebappContext webContext = new WebappContext("TestContext", context.getContextPath());
                context.getResourceConfig()
                        .register(new AbstractBinder() {
                            @Override
                            protected void configure() {
                                bind(webContext).to(ServletContext.class);
                            }
                        });
                //this.server = GrizzlyHttpServerFactory.createHttpServer(this.baseUri, context.getResourceConfig(), false);
                Map<String, String> params = new HashMap<>();
                params.put("jersey.config.server.provider.packages", "nl.mpi.shibboleth.metadata.rest");
                this.server = GrizzlyWebContainerFactory.create(this.baseUri, params);
                
                webContext.deploy(this.server);

            } catch (final ProcessingException cause) {
                throw new TestContainerException(cause);
            } catch(IOException cause) {
            throw new TestContainerException(cause);
            }
        }

        @Override
        public ClientConfig getClientConfig() {
            return null;
        }

        @Override
        public URI getBaseUri() {
            return baseUri;
        }

        @Override
        public void start() {
            if (server.isStarted()) {
                LOGGER.warning("Ignoring start request - RestTestContainer is already started");
            } else {
                LOGGER.fine("Starting RestTestContainer...");
                try {
                    server.start();
                    if (baseUri.getPort() == 0) {
                        baseUri = UriBuilder.fromUri(baseUri)
                                .port(server.getListener("grizzly").getPort())
                                .build();
                        LOGGER.info("Started GrizzlyTestContainer at the base URI " + baseUri);
                    }
                } catch (final ProcessingException | IOException cause) {
                    throw new TestContainerException(cause);
                }
            }
        }

        @Override
        public void stop() {
            if (server.isStarted()) {
                LOGGER.fine("Stopping RestTestContainer...");
                server.shutdownNow();
            } else {
                LOGGER.warning("Ignoring stop request - RestTestContainer is already stopped");
            }
        }
    }

    @Override
    public TestContainer create(final URI baseUri, final DeploymentContext context) {
        return new RestTestContainer(baseUri, context);
    }

}
