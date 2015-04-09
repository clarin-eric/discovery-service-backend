package nl.mpi.shibboleth.ds.metadata.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class ServletConfig extends AbstractServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ServletConfig.class);

    private final static String DEFAULT_title = "Clarin EU Service Provider";
    private final static String DEFAULT_subtitle = "Select your Provider";
    private final static String DEFAULT_metadataLocation = "./proxy";
    private final static String DEFAULT_helpMore = "If you cannot find your institution in the list above please select the \"Clarin.eu website account\" and use your credentials of the CLARIN website. For questions please contact webmaster@clarin.eu.";
    private final static String DEFAULT_noscript = "http://lux17.mpi.nl/Shibboleth.SSO/Login";
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    
        Config cfg = loadConfig(request.getServletContext());
        response.setContentType("application/json");        
        response.getWriter().println(mapper.writeValueAsString(cfg));
    }
    
    protected Config loadConfig(ServletContext ctxt) {
        Config cfg = new Config();
        cfg.title = getProperty(ctxt, "title", DEFAULT_title);
        cfg.subtitle = getProperty(ctxt, "subtitle", DEFAULT_subtitle);
        cfg.metadata.add(getProperty(ctxt, null, DEFAULT_metadataLocation));
        cfg.help = getProperty(ctxt, "provider-help", DEFAULT_helpMore);
        cfg.noscript = getProperty(ctxt, "noscipt-ds-fallback", DEFAULT_noscript);
        return cfg;
    }
    
    protected String getProperty(ServletContext ctxt, String name, String defaultValue) {
        if(name != null && ctxt.getInitParameter(name) != null) {
            return ctxt.getInitParameter(name);
        } else {
            return defaultValue;
        }
    }
    
    public static class Config {
        public String title;
        public String subtitle;
        public List<String> metadata = new ArrayList<String>();
        public String help;
        public String noscript;
    }
    
}
