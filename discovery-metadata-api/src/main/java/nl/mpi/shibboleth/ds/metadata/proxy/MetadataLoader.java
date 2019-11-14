package nl.mpi.shibboleth.ds.metadata.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class MetadataLoader {

    private static final Logger logger = LoggerFactory.getLogger(MetadataProxy.class);
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    private long lastModified = 0;
    
    private final static String DEFAULT_FEED = "idps_clarin.json";
    
    public String loadRawMetadata(ServletContext ctxt, String charset, String feedParam) throws MalformedURLException, IllegalStateException, UnsupportedEncodingException, IOException {
        String jsonMetadataDir = ctxt.getInitParameter("metadata-source");
        if(!jsonMetadataDir.endsWith("/")) {
            jsonMetadataDir+="/";
        }
        
        String feed = DEFAULT_FEED;
        if(feedParam != null && !feedParam.isEmpty()) {
            feed = feedParam;
        }        
        if(feed.startsWith("/")) {
            feed = feed.substring(1, feed.length());                   
        }
        
        
        String jsonMetadataFile = jsonMetadataDir+feed;
        if(jsonMetadataFile == null) {
            throw new IllegalStateException("Configuration problem. <metadata-source> is not configured.");
        }      
        
        
        logger.info("Status request for {}", jsonMetadataFile);
        
        lastModified = 0;
                
        BufferedReader br = null;
        URL url = new URL(jsonMetadataFile);
        if(url.getProtocol().equalsIgnoreCase("file")) {            
            String f = jsonMetadataFile.replaceAll("file://", "");
            lastModified = new File(f).lastModified();
            logger.info("Proxying local file [{}]", f);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        }/* else {
            logger.info("Proxying url [{}]", jsonMetadataFile);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            lastModified = httpConnection.getLastModified();
            br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), charset));
        }*/

        String line = null;
        StringBuilder discojuiceJsonBuffer = new StringBuilder();
        while((line=br.readLine()) != null) {
            discojuiceJsonBuffer.append(line.trim());
        }

        return discojuiceJsonBuffer.toString();
    }
    
    public DiscoJuiceJson loadMetadata(ServletContext ctxt, String charset, String feedParam) throws IllegalStateException, UnsupportedEncodingException, IOException {
        String json = loadRawMetadata(ctxt, charset, feedParam);
        return mapper.readValue("{\"idps\":"+json+"}", DiscoJuiceJson.class);
    }

    /**
     * @return the lastModified
     */
    public long getLastModified() {
        return lastModified;
    }
}
