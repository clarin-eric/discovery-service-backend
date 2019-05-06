package nl.mpi.shibboleth.ds.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author wilelb
 */
public class Statistics {

    public List<Country> countries = new ArrayList<Country>();
    public Time lastModified = null;
    public Long unkown = 0L;
    public State status = null;
    
    public void addCountry(String name) throws UnkownCountryException {
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
            throw new UnkownCountryException();
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
    
    public static class State {
        private boolean error;
        private String message;
        
        public static State ok() {
            State state = new State();
            state.error = false;
            state.message = null;
            return state;
        }
        
        public static State error(String message) {
            State state = new State();
            state.error = true;
            state.message = message;
            return state;
        }
        
        public State() {}
        
        

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }        
    }
    
    public static class Time {
        private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        private static TimeZone tz = TimeZone.getTimeZone("UTC");
        
        private long msSinceEpoch;
        private String iso8601;
        
        public Time() {
            this(new Date());
        }
        
        public Time(Date date) {
            df.setTimeZone(tz);
            msSinceEpoch = date.getTime();
            iso8601 = df.format(date);
        }
        
        public long getMsSinceEpoch() {
            return msSinceEpoch;
        }

        public void setMsSinceEpoch(long msSinceEpoch) {
            this.msSinceEpoch = msSinceEpoch;
        }

        public String getIso8601() {
            return iso8601;
        }

        public void setIso8601(String iso8601) {
            this.iso8601 = iso8601;
        }
    }
}
