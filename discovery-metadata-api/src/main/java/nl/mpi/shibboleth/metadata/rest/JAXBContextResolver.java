package nl.mpi.shibboleth.metadata.rest;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import nl.mpi.shibboleth.metadata.discojuice.DiscoJuiceJson;
import nl.mpi.shibboleth.metadata.discojuice.DiscoJuiceJsonObject;

/**
 * JAXB context resolver configure to output natural JSON.
 *
 * see: http://jersey.java.net/nonav/documentation/latest/json.html
 * 
 * @author wilelb
 */
@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;

    private Class<?>[] types = { DiscoJuiceJson.class, DiscoJuiceJsonObject.class };

    public JAXBContextResolver() throws Exception {
        JSONConfiguration config = JSONConfiguration.natural().build();
        context = new JSONJAXBContext(config, types);
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        for (Class<?> type : types) {
            if (type == objectType) {
                return context;
            }
        }
        return null;
    }
}
