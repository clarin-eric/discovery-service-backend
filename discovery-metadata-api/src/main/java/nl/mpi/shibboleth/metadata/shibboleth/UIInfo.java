package nl.mpi.shibboleth.metadata.shibboleth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * <md:Extensions>
    <mdui:UIInfo>
        <mdui:DisplayName xml:lang="nl">Cito</mdui:DisplayName>
        <mdui:DisplayName xml:lang="en">Cito</mdui:DisplayName>
        <mdui:Description xml:lang="nl">Cito</mdui:Description>
        <mdui:Description xml:lang="en">Cito</mdui:Description>
        <mdui:Keywords xml:lang="nl">cito</mdui:Keywords>
    <mdui:Keywords xml:lang="en">cito</mdui:Keywords>
    </mdui:UIInfo>
</md:Extensions>
 */

public class UIInfo {
    private List<Keywords> keywords = new ArrayList<>();
    private List<Name> names = new ArrayList<>();
    private List<Description> descriptions = new ArrayList<>();
    private Logo logo = new Logo();
    
    @XmlElement(name="DisplayName", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    @XmlElement(name="Description", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public List<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Description> descriptions) {
        this.descriptions = descriptions;
    }

    @XmlElement(name="Keywords", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public List<Keywords> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keywords> keywords) {
        this.keywords = keywords;
    }

    @XmlElement(name="Logo", namespace="urn:oasis:names:tc:SAML:metadata:ui")
    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }
    
    public static class Name {
    
        private String name;
        private String lang;
        
        @XmlAttribute(name = "lang", namespace="http://www.w3.org/XML/1998/namespace")
        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        /**
         * @return the name
         */
        @XmlValue
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
    }
    
    public static class Description {
        @XmlValue
        public String value;
        @XmlAttribute(name = "lang", namespace="http://www.w3.org/XML/1998/namespace")
        public String lang;
    }
    
    public static class Keywords {
        @XmlValue
        public String value;
        @XmlAttribute(name = "lang", namespace="http://www.w3.org/XML/1998/namespace")
        public String lang;
    }
    
    public static class Logo {
        @XmlValue
        public String url = "";
        @XmlAttribute
        public int height = 0;
        @XmlAttribute
        public int width = 0;
    }
}
