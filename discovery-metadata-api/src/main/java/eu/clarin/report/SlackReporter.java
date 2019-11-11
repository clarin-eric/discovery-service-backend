/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarin.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.bind.annotation.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class SlackReporter implements Reporter {

    private final static Logger logger = LoggerFactory.getLogger(SlackReporter.class);
    
    private final String urlString;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    protected final static String SLACK_URL = "reporting.slack.url";
    
    public SlackReporter(Properties props) {
        if(!props.containsKey(SLACK_URL)) {
            throw new IllegalStateException("Slack url ("+SLACK_URL+") is required.");
        }
        this.urlString = props.getProperty(SLACK_URL);
    }
    
    @Override
    public boolean sendReport(String message) {
        boolean result = false;
        
        SlackMessage msg = new SlackMessage();
        msg.setText(message);
            
        try {
            URL url = new URL (urlString);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            
            //Write request body
            OutputStream os = con.getOutputStream();
            String jsonInputString = objectMapper.writeValueAsString(msg);
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           

            //Read response body
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            logger.info("Slack webhook response: {}", response.toString());
            
            result = true;
        } catch(JsonProcessingException ex) {
            logger.error("", ex);
        } catch(MalformedURLException ex) {
            logger.error("", ex);
        } catch(IOException ex) {
            logger.error("", ex);
        }
        
        return result;
    }
    
    private static class SlackMessage {
        private String text;

        @XmlElement(name = "text")
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
