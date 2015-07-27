package eu.clarin.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author wilelb
 */
public class TldUtils {

    public static String source = "https://publicsuffix.org/list/public_suffix_list.dat";

    private static TldUtils _instance;

    /**
     * Parse the gTLD or ccTLD from the url and return the country code for that
     * ccTLD, or null if it is a gTLD
     *
     * @param url
     * @return
     */
    public static String getTldCountryCode(String url) {
        if (_instance == null) {
            _instance = new TldUtils();
        }
        String tld = _instance.getTldFromUrl(url);
        return _instance.getCountryCodeForTld(tld);
    }

    private final List<Tld> tlds = new LinkedList<>();

    private TldUtils() {
        loadData();
    }

    private String getTldFromUrl(String url) {
        try {
            String host = new URL(url).getHost();
            int index = host.lastIndexOf(".");
            if (index >= 0) {
                return host.substring(index);
            }
        } catch (MalformedURLException ex) {

        }
        return null;
    }

    private String getCountryCodeForTld(String tld) {
        if (tld != null) {
            for (Tld t : tlds) {
                if (t.getTld().equalsIgnoreCase(tld)) {
                    return t.getCode();
                }
            }
        }
        return null;
    }

    private void loadData() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("tlds.csv");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] records = line.split(",");
                this.tlds.add(new Tld(records[0], records[1], records[2]));
            }
        } catch (IOException ex) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private static class Tld {

        private final String tld;
        private final String country;
        private final String code;

        public Tld(String tld, String country, String code) {
            this.tld = tld;
            this.country = country;
            this.code = code.toUpperCase();
        }

        /**
         * @return the tld
         */
        public String getTld() {
            return tld;
        }

        /**
         * @return the country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }
    }
}
