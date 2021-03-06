package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author wilelb
 */
public class IDPSSoDescriptor {

    private List<SingleSignOnService> ssos = new ArrayList<SingleSignOnService>();
    private Extensions extensions;
    
    @XmlElement(name="SingleSignOnService", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<SingleSignOnService> getSsos() {
        return ssos;
    }

    public void setSsos(List<SingleSignOnService> ssos) {
        this.ssos = ssos;
    }

    @XmlElement(name="Extensions",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }
}
