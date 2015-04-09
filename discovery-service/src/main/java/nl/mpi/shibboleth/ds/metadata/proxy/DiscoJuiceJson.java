package nl.mpi.shibboleth.ds.metadata.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilelb
 */
public class DiscoJuiceJson {
    
    public List<Idp> idps = new ArrayList<Idp>();
    
    public static class Idp {    
        public String entityID;
        public List<Title> titles = new ArrayList<Title>();
        public String country;
        public Geo geo;
        public Integer weight = 0;
        public Icon icon;
    }

    public static class Geo {
        public Float lat;
        public Float lon;   
        
        public Geo() {}
        public Geo(Float lat, Float lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
    
    public static class Title {
        public String language;
        public String value;

        public Title() {}
        
        public Title(String language, String value) {
            this.language = language;
            this.value = value;
        }         
    }
    
    public static class Icon {
        public String url;
        public Integer width;
        public Integer height;
        
        public Icon() {}
        
        public Icon(String url, Integer width, Integer height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }
    }    
}
