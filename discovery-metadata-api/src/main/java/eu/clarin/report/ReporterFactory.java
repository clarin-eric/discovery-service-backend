package eu.clarin.report;

import eu.clarin.discovery.federation.AuthoritiesMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class ReporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(ReporterFactory.class);
    
    protected final static String SLACK_ENABLED = "reporting.slack.enabled";
    protected final static String EMAIL_ENABLED = "reporting.email.enabled";
    
    public static void loadFromProperties(String propertiesFile, AuthoritiesMapper mapper) {
        if(propertiesFile != null) {          
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(propertiesFile));
            } catch(IOException ex) {
                logger.error("Failed to load properties file.", ex); 
            }
            
            //Configure slack reporter if it is configured
            if(Boolean.valueOf(props.getProperty("SLACK_ENABLED"))) {
                mapper.addReporter(new SlackReporter(props));
                logger.info("Added slack reporter");
            }

           //Configure email reporter
           /*
           String host = "smtp.transip.email";
           int port = 465;
           boolean tls = true;
           boolean auth = true;
           String username  = "willem@clarin.eu";
           String password = ""; 
           String from = "willem@clarin.eu";
           String to = "willem@clarin.eu";
           String subject = "Reporter test message";
           */
           if(Boolean.valueOf(props.getProperty("EMAIL_ENABLED"))) {
                mapper.addReporter(new EmailReporter(props));
                logger.info("Added email reporter");
           }
        } else {
            logger.info("No reporters configured");
        }
    }
}
