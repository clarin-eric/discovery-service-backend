package eu.clarin.url;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author wilelb
 */
public class TldUtilsTest {

    @Test
    public void testTldFromUrlToCountryCode() {
        String url = "https://shibboleth.fh-vie.ac.at/idp/shibboleth";
        String code = TldUtils.getTldCountryCode(url);
        
        Assert.assertEquals("AT", code);
    }
}
