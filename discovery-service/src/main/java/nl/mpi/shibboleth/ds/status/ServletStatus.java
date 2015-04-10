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
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext ctxt = request.getSession().getServletContext();
        //ServletContext ctxt = request.getServletContext();
        
        String charset = "UTF-8";
        
        MetadataLoader loader = new MetadataLoader();
        DiscoJuiceJson json = loader.loadMetadata(ctxt, charset);
        
        Statistics stats = new Statistics();
        for(Idp idp : json.idps) {
            stats.addCountry(idp.country);
        }
        
        response.setContentType("application/json");//charset="+charset);
        PrintWriter out = response.getWriter();
        out.println(mapper.writeValueAsString(stats));
    }
}
