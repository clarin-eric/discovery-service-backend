package nl.mpi.shibboleth.metadata.discojuice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * IDP metadata:
 *  https://wiki.shibboleth.net/confluence/display/SHIB2/MetadataForIdP
 * 
 * IDP MD UI extension:
 *  https://wiki.shibboleth.net/confluence/display/SHIB2/IdPMDUIRecommendations
 * 
 * @author wilelb
 */
@XmlRootElement
public class DiscoJuiceJsonObject {

    private String entityID;
    private List<Title> titles = new ArrayList<>();
    private String country;
    private int weight = 0;
    private Icon icon;

    /**
     * @return the entityID
     */
    @XmlElement(name = "entityID")
    public String getEntityID() {
        return entityID;
    }

    /**
     * @param entityID the entityID to set
     */
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    /**
     * @return the countryCode
     */
    @XmlElement(name = "country")
    public String getCountry() {
        return country;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountry(String countryCode) {
        this.country = countryCode;
    }

    /**
     * @return the weight
     */
    @XmlElement(name = "weight")
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the titles
     */
    public List<Title> getTitles() {
        return titles;
    }

    /**
     * @param titles the titles to set
     */
    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }
    
    public void addTitle(Title t) {
        this.titles.add(t);
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public static class Title {
        private String language;
        private String value;

        public Title() {}
        
        public Title(String language, String value) {
            this.language = language;
            this.value = value;
        }
        
        /**
         * @return the language
         */
        public String getLanguage() {
            return language;
        }

        /**
         * @param language the language to set
         */
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
                
    }
    
    public static class Icon {
        public String url;
        public Integer width;
        public Integer height;
    }
}