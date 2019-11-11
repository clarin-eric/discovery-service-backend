package eu.clarin.report;

import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author wilelb
 */
public class ReporterTest {

    @Test
    public void sendSlackMessageTest() {
        Properties props = new Properties();
        props.put(SlackReporter.SLACK_URL, "https://hooks.slack.com/services/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");
        
        Reporter reporter = new SlackReporter(props);
        boolean result = reporter.sendReport("Test line 1\nline2\nline3");
        Assert.assertTrue(result);
    }
    
    //@Test
    public void sendEmailMessageTest() {
        Properties props = new Properties();
        props.put(EmailReporter.EMAIL_FROM, "willem@clarin.eu");
        props.put(EmailReporter.EMAIL_TO, "willem@clarin.eu");        
        props.put(EmailReporter.EMAIL_SUBJECT, "Reporter test message");
        
        props.put(EmailReporter.SMTP_AUTH, "true");
        props.put(EmailReporter.SMTP_AUTH_USERNAME, "willem@clarin.eu");
        props.put(EmailReporter.SMTP_AUTH_PASSWORD, "");
        
        
        props.put("mail.smtp.host", "smtp.transip.email");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.transip.email");
                
        Reporter reporter = new EmailReporter(props);
        boolean result = reporter.sendReport("Test line 1\nline2\nline3");
        Assert.assertTrue(result);
    }
}
