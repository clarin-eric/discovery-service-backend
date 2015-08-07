package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author wilelb
 */
public class EntityDescriptor {

    private List<IDPSSoDescriptor> idpSsoDescriptors = new ArrayList<IDPSSoDescriptor>();
    private List<Organization> organizations = new ArrayList<Organization>();
    private List<ContactPerson> contacts = new ArrayList<ContactPerson>();
    private String entityID;
    
    @XmlElement(name="Extensions", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public EntityExtensions extensions;
    
    @XmlAttribute(name="entityID")
    public String getEntityID() {
        return entityID;
    }
    
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    @XmlElement(name="IDPSSODescriptor",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<IDPSSoDescriptor> getIdpSsoDescriptors() {
        return idpSsoDescriptors;
    }

    public void setIdpSsoDescriptors(List<IDPSSoDescriptor> idpSsoDescriptors) {
        this.idpSsoDescriptors = idpSsoDescriptors;
    }

    @XmlElement(name="Organization",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
        
    @XmlElement(name="ContactPerson",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<ContactPerson> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactPerson> contacts) {
        this.contacts = contacts;
    }
    
    public boolean isIDPDescriptor() {
        return idpSsoDescriptors != null && idpSsoDescriptors.size() > 0;
    }

    public boolean isSPDescriptor() {
        return false; //TODO: implement
    }
}
