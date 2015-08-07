package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class EntityExtensions {
    @XmlElement(name = "RegistrationInfo", namespace = "urn:oasis:names:tc:SAML:metadata:rpi")
    public RegistrationInfo registrationInfo;
    
    @XmlElementWrapper(name = "EntityAttributes", namespace = "urn:oasis:names:tc:SAML:metadata:attribute")
    @XmlElement(name = "Attribute", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    public List<EntityAttribute> attributes;
    
    public static class RegistrationInfo {
        @XmlAttribute(name = "registrationAuthority=")
        public String registrationAuthority;
        @XmlAttribute(name = "registrationInstant")
        public String registrationInstant;
    }
       
    public static class EntityAttribute {
        @XmlAttribute(name = "Name")
        public String name;
        @XmlAttribute(name = "NameFormat")
        public String nameFormat;
        @XmlElement(name="AttributeValue", namespace="urn:oasis:names:tc:SAML:2.0:assertion")
        public String value;
    }
}
