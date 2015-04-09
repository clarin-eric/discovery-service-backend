package nl.mpi.shibboleth.metadata.shibboleth;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author wilelb
 */
public class SingleSignOnService {
    private String binding;
    private String location;

    @XmlAttribute(name="Binding")
    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    @XmlAttribute(name="Location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
