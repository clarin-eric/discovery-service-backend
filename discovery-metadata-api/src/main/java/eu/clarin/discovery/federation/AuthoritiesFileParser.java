/*
 * Parse the federation mapping file
 */
package eu.clarin.discovery.federation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class AuthoritiesFileParser {
    
    private static Logger logger = LoggerFactory.getLogger(AuthoritiesFileParser.class);
    
    private class CustomValidationEventHandler implements ValidationEventHandler{
        @Override
        public boolean handleEvent(ValidationEvent evt) {
            //Ignore unexpected elements, but log other validation errors.
            if(!evt.getMessage().contains("Unexpected element")) {
                logger.trace("{}",evt);
            }
            return true;
        }
    }
    
    public Authorities parse(URL url)  {
        Authorities map = null;
        InputStreamReader rdr = null;
        
        try {
            rdr = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);           
          
            JAXBContext context = JAXBContext.newInstance(Authorities.class);
            Unmarshaller u = context.createUnmarshaller();
            u.setEventHandler(new CustomValidationEventHandler());
            map = (Authorities) u.unmarshal(rdr);
        } catch(JAXBException ex) {
            logger.error("Failed to unmarshall xml.", ex); 
        } catch(IOException ex) {
            logger.error("Failed to read url.", ex);
        } finally {
            if(rdr != null) {
                try {
                    rdr.close();
                } catch(IOException ex) {
                    logger.error("Failed to close reader.", ex);
                }
            }
        }
        
        return map;
    }
}
    