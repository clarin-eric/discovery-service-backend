package nl.mpi.shibboleth.ds.status;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilelb
 */
public class Statistics {

    public List<Country> countries = new ArrayList<Country>();
    public Long lastModified = 0L;
    public Long unkown = 0L;
    
    public void addCountry(String name) {
        for(Country c : countries) {
            if(c.name != null && c.name.equalsIgnoreCase(name)) {
                c.increment();
                return;
            }
        }
        
        if(name != null) {
            countries.add(new Country(name));
        } else {
            unkown++;
        }
    }
    
    public static class Country {
        public String name;
        public Long count = 0L;
        
        public Country() {}
        
        public Country(String name) {
            this.name = name;
            this.count++;                   
        }
        
        public void increment() {
            count++;
        }
    }
}
