package eu.clarin.discovery.federation;

import eu.clarin.discovery.federation.Authorities.Authority;
import eu.clarin.report.Reporter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map registration authorities to country codes while keeping track of 
 * (new) authorities that cannot map to a country code.
 * 
 * @author wilelb
 */
public class AuthoritiesMapper {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthoritiesMapper.class);
    
    private final Authorities authorities;
    private final Set<String> unkownAuthorities;
    private final List<Reporter> reporters;
    
    public AuthoritiesMapper(Authorities authorities) {
        this.authorities = authorities;
        this.unkownAuthorities = new HashSet<>();
        this.reporters = new ArrayList<>();
    }
    
    public void addReporter(Reporter reporter) {
        this.reporters.add(reporter);
    }
           
    public Authority getCountryCodeForRegistrationAuthority(String registrationAuthority) {
        Authority authority = authorities.getAuthority(registrationAuthority);
        if(authority == null) {
            unkownAuthorities.add(registrationAuthority);
        }
        return authority;
    }
    
    public void report() {
        if(unkownAuthorities.size() > 0) {            
            unkownAuthorities.forEach((a) -> {
                logger.warn("Unkown registration authority: {}", a);
            });
            
            //Send message with each configured reporter
            reporters.forEach((r) -> { r.sendReport(buildReportMessage()); });
        }
    }
    
    private String buildReportMessage() {
        String message = "The following registrations authorities are unkown:\n";
        message += unkownAuthorities.stream().collect(Collectors.joining(" - ", "\n", ""));
        return message;
    }
}
