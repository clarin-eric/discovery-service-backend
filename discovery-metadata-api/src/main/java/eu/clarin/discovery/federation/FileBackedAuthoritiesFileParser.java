/*
 * Parse the federation mapping file
 */
package eu.clarin.discovery.federation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
public class FileBackedAuthoritiesFileParser implements AuthoritiesFileParser {
    
    private static Logger logger = LoggerFactory.getLogger(FileBackedAuthoritiesFileParser.class);
    
    private final File fileLocation;
    private final File tempFileLocation;
    
    public FileBackedAuthoritiesFileParser(String fileLocation) {
        this.fileLocation = new File(fileLocation);
        this.tempFileLocation = new File(fileLocation+".tmp");
    }
    
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
    
    /**
     * Download file from the supplied url to the temporary location on disk
     * @param url
     * @throws IOException 
     */
    protected void download(URL url) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            fileOutputStream = new FileOutputStream(this.tempFileLocation);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel()
                .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } finally {
            if(fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
    
    /**
     * Validate the downloaded file and throw an exception otherwise
     * @param src 
     */
    protected void validate(File src) {
        //Not implemented, should throw an exception on failed validation
    }
    
    @Override
    public Authorities parse(URL url)  {
        //Try to download latest version if the file
        try {
            logger.debug("Downloading {} to file {}, using temp file {}", url.toString(), this.fileLocation, this.tempFileLocation);
            download(url);
            validate(this.tempFileLocation);
            Files.move(
                    this.tempFileLocation.toPath(), 
                    this.fileLocation.toPath(), 
                    StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            logger.warn("Failed to download latest version of authorities mapping file from "+url.toString()+" to "+this.fileLocation, ex);
        }
        
        if(!this.fileLocation.exists()) {
            
        }
        
        //Load the authories from disk
        Authorities map = null;
        InputStreamReader rdr = null;
        try {
            rdr = new InputStreamReader(new FileInputStream(this.fileLocation));
          
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
    