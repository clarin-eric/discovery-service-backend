package eu.clarin.report;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Reference:
 *      https://www.baeldung.com/java-email
 * 
 *      https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
 *      https://javaee.github.io/javamail/docs/api/com/sun/mail/pop3/package-summary.html
 *      https://javaee.github.io/javamail/docs/api/com/sun/mail/imap/package-summary.html
 * 
 * @author wilelb
 */
public class EmailReporter implements Reporter {
    
    private final static Logger logger = LoggerFactory.getLogger(EmailReporter.class);
    
    private final Properties props;
    private final String from;
    private final String to;
    private final String subject;
    
    protected final static String SMTP_AUTH = "mail.smtp.auth";
    protected final static String SMTP_AUTH_USERNAME = "reporting.email.username";
    protected final static String SMTP_AUTH_PASSWORD = "reporting.email.password";
    
    protected final static String EMAIL_FROM = "reporting.email.from";
    protected final static String EMAIL_TO = "reporting.email.to";
    protected final static String EMAIL_SUBJECT = "reporting.email.subject";
   
    public EmailReporter(Properties props) {
        this.props = props;
        
        //Ensure we have a username and password if smtp authentication is enabled
        if(Boolean.valueOf(props.getProperty(SMTP_AUTH))) {
            if(!props.containsKey(SMTP_AUTH_USERNAME) || props.getProperty(SMTP_AUTH_USERNAME).length() <= 0) {
                throw new IllegalStateException("SMTP username is required when "+SMTP_AUTH+"=true.");
            }
            if(!props.containsKey(SMTP_AUTH_PASSWORD) || props.getProperty(SMTP_AUTH_PASSWORD).length() <= 0) {
                throw new IllegalStateException("SMTP password is required when "+SMTP_AUTH+"=true.");
            }
        }
        
        if(!props.containsKey(EMAIL_FROM) || props.getProperty(EMAIL_FROM).length() <= 0) {
            throw new IllegalStateException("From address ("+EMAIL_FROM+") is required");
        }
        if(!props.containsKey(EMAIL_TO) || props.getProperty(EMAIL_TO).length() <= 0) {
            throw new IllegalStateException("To address ("+EMAIL_TO+") is required");
        }
        if(!props.containsKey(EMAIL_SUBJECT) || props.getProperty(EMAIL_SUBJECT).length() <= 0) {
            throw new IllegalStateException("Email subject ("+EMAIL_SUBJECT+") is required");
        }
        
        this.from = props.getProperty(EMAIL_FROM);
        this.to = props.getProperty(EMAIL_TO);
        this.subject = props.getProperty(EMAIL_SUBJECT);
    }
    
    @Override
    public boolean sendReport(String message) {
        boolean result = false;
        try {
            Session session = Session.getInstance(this.props);
            if(Boolean.valueOf(props.getProperty(SMTP_AUTH))) {
                session = Session.getInstance(this.props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(props.getProperty(SMTP_AUTH_USERNAME), props.getProperty(SMTP_AUTH_PASSWORD));
                    }
                });
            }
            
            Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress(to));
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(from));
            email.setSubject(this.subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(message, "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            email.setContent(multipart);

            Transport.send(email);
            
            result = true;
        } catch(MessagingException ex) {
            logger.error("", ex);
        }        
        return result;
    }
    
}
