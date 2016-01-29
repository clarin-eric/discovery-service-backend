package nl.mpi.shibboleth.ds.status;

import nl.mpi.shibboleth.ds.AbstractServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mpi.shibboleth.ds.metadata.proxy.DiscoJuiceJson;
import nl.mpi.shibboleth.ds.metadata.proxy.DiscoJuiceJson.Idp;
import nl.mpi.shibboleth.ds.metadata.proxy.MetadataLoader;
import nl.mpi.shibboleth.ds.metadata.proxy.MetadataProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class ServletStatus extends AbstractServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(MetadataProxy.class);
    
    private final ObjectMapper mapper = new ObjectMapper();
    private final MetadataLoader loader = new MetadataLoader();
    
    private static final String CHARSET = "UTF-8";
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //servlet 2.0 spec:
        ServletContext ctxt = request.getSession().getServletContext();
        //servlet 3.0 spec:
        //ServletContext ctxt = request.getServletContext();        
        
        DiscoJuiceJson json = loader.loadMetadata(ctxt, CHARSET);
        
        Statistics stats = new Statistics();
        stats.lastModified = loader.getLastModified();
        for(Idp idp : json.idps) {
            try {
                stats.addCountry(idp.country);                
            } catch(UnkownCountryException ex) {
                logger.info("Unkown country [{}] for idp: {}", idp.country, idp.entityID);
            }
        }
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(mapper.writeValueAsString(stats));
    }
}
