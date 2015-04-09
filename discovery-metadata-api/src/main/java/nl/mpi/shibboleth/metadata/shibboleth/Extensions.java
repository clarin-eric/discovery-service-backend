package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class Extensions {
    private UIInfo info;
    
    @XmlElement(name="DiscoHints", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public DiscoHints disco;
    
    @XmlElement(name="UIInfo", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public UIInfo getInfo() {
        return info;
    }

    public void setInfo(UIInfo info) {
        this.info = info;
    }
    
    public static class DiscoHints {
        @XmlElement(name="IPHints", namespace="urn:oasis:names:tc:SAML:metadata:ui")
        public List<String> iPHints = new ArrayList<String>();
        @XmlElement(name="DomainHint", namespace="urn:oasis:names:tc:SAML:metadata:ui")
        public String domainHint;
        @XmlElement(name="GeolocationHint", namespace="urn:oasis:names:tc:SAML:metadata:ui")
        public String geolocationHint;
    }
}
