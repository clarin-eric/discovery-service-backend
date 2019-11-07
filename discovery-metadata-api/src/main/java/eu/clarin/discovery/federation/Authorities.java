package eu.clarin.discovery.federation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilelb
 */
@XmlRootElement(name = "authorities")
public class Authorities {

    private List<Federation> federations = new ArrayList<>();

    public Authorities() {
    }

    @XmlElement(name = "authority")
    public List<Federation> getFederations() {
        return federations;
    }

    public void setFederations(List<Federation> federations) {
        this.federations = federations;
    }
    
    public Federation getFederation(String key) {
        for(Federation f: federations) {
            if(f.key.equalsIgnoreCase(key)) {
                return f;
            }
        }
        return null;
    }

    public boolean hasFederation(String key) {
        for(Federation f: federations) {
            if(f.key.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
    
    public static class Federation {

        private String key;
        private String code;

        @XmlElement(name = "key")
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