package eu.clarin.discovery.federation;

import eu.clarin.discovery.federation.Authorities.Authority;
import java.util.HashSet;
import java.util.Set;
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
    
    public AuthoritiesMapper(Authorities authorities) {
        this.authorities = authorities;
        this.unkownAuthorities = new HashSet<>();
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
            //TODO: report via mail or slack hook
        }
    }
}
