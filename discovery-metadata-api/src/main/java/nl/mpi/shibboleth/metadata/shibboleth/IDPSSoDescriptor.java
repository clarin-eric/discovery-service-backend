package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author wilelb
 */
public class IDPSSoDescriptor {

    private List<SingleSignOnService> ssos = new ArrayList<SingleSignOnService>();
    //private List<UIInfo> uiInfos = new ArrayList<UIInfo>();
    //private String protocolSupportEnumeration;
    private Extensions extensions;
    
    @XmlElement(name="SingleSignOnService", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public List<SingleSignOnService> getSsos() {
        return ssos;
    }

    public void setSsos(List<SingleSignOnService> ssos) {
        this.ssos = ssos;
    }
/*
    @XmlElementWrapper(name="Extensions",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    @XmlElement(name="UIInfo", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public List<UIInfo> getUiInfos() {
        return uiInfos;
    }

    public void setUiInfos(List<UIInfo> uiInfos) {
        this.uiInfos = uiInfos;
    }
*/
/*
    @XmlAttribute(name="protocolSupportEnumeration=")
    public String getProtocolSupportEnumeration() {
        return protocolSupportEnumeration;
    }

    public void setProtocolSupportEnumeration(String protocolSupportEnumeration) {
        this.protocolSupportEnumeration = protocolSupportEnumeration;
    }
*/

    @XmlElement(name="Extensions",namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }
}
