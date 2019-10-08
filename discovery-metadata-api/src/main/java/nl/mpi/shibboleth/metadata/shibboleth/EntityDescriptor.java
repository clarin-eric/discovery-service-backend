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

    private List<IDPSSoDescriptor> idpSsoDescriptors = new ArrayList<>();
    private List<Organization> organizations = new ArrayList<>();
    private List<ContactPerson> contacts = new ArrayList<>();
    private String entityID;
    
    @XmlElement(name="Extensions", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    private EntityExtensions extensions;
    
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

    /**
     * @return the extensions
     */
    public EntityExtensions getExtensions() {
        return extensions;
    }
    
    public boolean hideFromDiscovery() {
        boolean hide = false;
        if(getExtensions() != null && getExtensions().attributes != null) {
            for(EntityExtensions.EntityAttribute a : getExtensions().attributes) {
                boolean entityCat = a.name.equalsIgnoreCase("http://macedir.org/entity-category");
                boolean hideFromDiscovery = a.value.equalsIgnoreCase("http://refeds.org/category/hide-from-discovery");
                if(entityCat && hideFromDiscovery) {
                    hide = true;
                }
            }
        }
        return hide;
    }
}
