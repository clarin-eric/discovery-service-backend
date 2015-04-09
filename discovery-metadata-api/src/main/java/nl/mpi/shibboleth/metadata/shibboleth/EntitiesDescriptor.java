package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilelb
 */
@XmlRootElement(name="EntitiesDescriptor", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
public class EntitiesDescriptor {
    private List<EntityDescriptor> entityDescriptor = new ArrayList<EntityDescriptor>();

    public EntitiesDescriptor() {}

    @XmlElement(name="EntityDescriptor", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<EntityDescriptor> getEntityDescriptor() {
        return entityDescriptor;
    }

    public void setEntityDescriptor(List<EntityDescriptor> entityDescriptor) {
        this.entityDescriptor = entityDescriptor;
    }
}
