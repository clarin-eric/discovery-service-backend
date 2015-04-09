package nl.mpi.shibboleth.metadata;

import nl.mpi.shibboleth.metadata.shibboleth.EntitiesDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * Use the apache commons digester to parse shibboleth metadata xml files
 * @author wilelb
 */
public class MetadataParser {

    private static Logger logger = LoggerFactory.getLogger(MetadataParser.class);

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

    public MetadataParser() {
    }

    public EntitiesDescriptor parse(URL url) {
        EntitiesDescriptor descriptor = null;
        try {
            InputSource input = new InputSource(url.openStream());
            descriptor = parseJaxb(input);
        } catch(IOException ex) {
            logger.error("Failed to read url.", ex);
        }
        return descriptor;
    }
    
    public EntitiesDescriptor parse(String xml) {
        InputSource input = new InputSource(new StringReader((xml)));
        return parseJaxb(input);
    }
    
    public EntitiesDescriptor parseJaxb(InputSource input) {
        EntitiesDescriptor descriptor = null;
        try {        
            JAXBContext context = JAXBContext.newInstance(EntitiesDescriptor.class);
            Unmarshaller u = context.createUnmarshaller();
            u.setEventHandler(new CustomValidationEventHandler());
            descriptor = (EntitiesDescriptor) u.unmarshal(input);
        } catch(JAXBException ex) {
            logger.error("Failed to unmarshall xml.", ex);
        } 
        return descriptor;
    }
}
