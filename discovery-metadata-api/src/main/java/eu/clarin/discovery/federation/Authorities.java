package eu.clarin.discovery.federation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author wilelb
 */
@XmlRootElement(name = "authorities")
public class Authorities {

    private List<Authority> authorities = new ArrayList<>();

    public Authorities() {
    }

    @XmlElement(name = "authority")
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
    
    public Authority getAuthority(String key) {
        for(Authority f: authorities) {
            if(f.getKey().equalsIgnoreCase(key)) {
                return f;
            }
        }
        return null;
    }

    public boolean hasAuthority(String key) {
        for(Authority f: authorities) {
            if(f.key.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
    
    public static class Authority {

        private String key;
        private String code;

        @XmlValue
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @XmlAttribute(name = "code")
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}