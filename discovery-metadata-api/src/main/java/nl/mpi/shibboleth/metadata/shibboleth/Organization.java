package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *  <Organization>
 *      <OrganizationName xml:lang="en">...</OrganizationName>
 *      <OrganizationDisplayName xml:lang="en">...</OrganizationDisplayName>
 *      <OrganizationURL xml:lang="en">...</OrganizationURL>
 *  </Organization>
 */
public class Organization {
    private List<Value> names;
    private List<Value> displayNames;
    private String url;
    private String lang;

    @XmlElement(name="OrganizationName", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<Value> getNames() {
        return names;
    }
    
    public void setNames(List<Value> names) {
        this.names = names;
    }

    @XmlElement(name="OrganizationDisplayName", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<Value> getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(List<Value> displayNames) {
        this.displayNames = displayNames;
    }

    @XmlElement(name="OrganizationURL", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class Value {
        @XmlValue
        public String value;
        @XmlAttribute(name = "lang", namespace="http://www.w3.org/XML/1998/namespace")
        public String lang;
    }
}
