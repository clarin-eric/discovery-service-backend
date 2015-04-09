package nl.mpi.shibboleth.ds.metadata.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wilelb
 */
public class Statistics {

    public List<Country> countries = new ArrayList<Country>();
    
    public void addCountry(String name) {
        for(Country c : countries) {
            if(c.name.equalsIgnoreCase(name)) {
                c.increment();
                return;
            }
        }
        
        countries.add(new Country(name));
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
