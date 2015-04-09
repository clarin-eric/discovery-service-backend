package nl.mpi.shibboleth.metadata.shibboleth;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *  <ContactPerson contactType="...">
 *      <SurName>...</SurName>
 *      <EmailAddress>...</EmailAddress>
 *  </ContactPerson>
 */
public class ContactPerson {

    private String contactType;
    private String surname;
    private String emailAddress;

    @XmlAttribute(name="contactType")
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    
    @XmlElement(name="SurName", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @XmlElement(name="EmailAddress", namespace="urn:oasis:names:tc:SAML:2.0:metadata")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
