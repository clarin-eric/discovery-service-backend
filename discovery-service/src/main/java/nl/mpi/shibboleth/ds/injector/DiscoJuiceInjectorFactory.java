package nl.mpi.shibboleth.ds.injector;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.ServletContext;


/**
 *
 * @author wilelb
 */
public class DiscoJuiceInjectorFactory {

    private final static String DEFAULT_title = "Clarin EU Service Provider";
    private final static String DEFAULT_subtitle = "Select your Provider";
    private final static String DEFAULT_metadataLocation = "./proxy";
    private final static String DEFAULT_helpMore = "If you cannot find your institution in the list above please select the \"Clarin.eu website account\" and use your credentials of the CLARIN website. For questions please contact webmaster@clarin.eu.";
    private final static String DEFAULT_noscript = "http://lux16.mpi.nl/Shibboleth.SSO/Login";
    private final static String DEFAULT_version = "?";
    
    public static AbstractDiscoJuiceInjector getDiscoJuice10Injector(ServletContext ctxt) {
        throw new UnsupportedOperationException("DiscoJuice 1.0 is not supported anymore.");
    }
    
    public static AbstractDiscoJuiceInjector getDiscoJuice20Injector(ServletContext ctxt) {
        DiscoJuice20Injector injector = new DiscoJuice20Injector();
        initializeInjector(injector, ctxt);
        return injector;
    }
    
    private static void initializeInjector(AbstractDiscoJuiceInjector i, ServletContext ctxt) {        
        if(ctxt.getInitParameter("title") != null) {
            i.setTitle(ctxt.getInitParameter("title"));
        } else {
            i.setTitle(DEFAULT_title);
        }
        if(ctxt.getInitParameter("subtitle") != null) {
            i.setSubtitle(ctxt.getInitParameter("subtitle"));
        } else {
            i.setSubtitle(DEFAULT_subtitle);
        }

        i.setMetadataLocation(DEFAULT_metadataLocation);

        if(ctxt.getInitParameter("provider-help") != null) {
            i.setHelpMore(ctxt.getInitParameter("provider-help"));
        } else {
            i.setHelpMore(DEFAULT_helpMore);
        } 
        if(ctxt.getInitParameter("noscipt-ds-fallback") != null) {
            i.setNoScriptFallback(ctxt.getInitParameter("noscipt-ds-fallback") );
        } else {
            i.setNoScriptFallback(DEFAULT_noscript);
        }
        if(ctxt.getInitParameter("version") != null) {
            i.setVersion(ctxt.getInitParameter("version") );
        } else {
            i.setVersion(DEFAULT_version);
        }
        //String serverName = InetAddress.getLocalHost().getHostName();
        try {
            String serverName = InetAddress.getLocalHost().getHostName();
            if(serverName.endsWith(".clarin.eu")) {
                serverName = serverName.substring(0, serverName.length()-10);
            }
            i.setServer(serverName);
        } catch(UnknownHostException ex) {
            i.setServer("n/a");
        }
        
    }
}
