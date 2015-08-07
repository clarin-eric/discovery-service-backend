package nl.mpi.shibboleth.ds;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.mpi.shibboleth.ds.injector.AbstractDiscoJuiceInjector;
import nl.mpi.shibboleth.ds.injector.DiscoJuiceInjectorFactory;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Return a "dynamic" version of the discojuice idp.html
 * Based on discojuice (www.discojuice.org) version 2.0
 * 
 * Binaries: https://engine.discojuice.org/
 * Sources: https://github.com/andreassolberg/DiscoJuice
 * 
 * @author wilelb
 */
public class ServletDiscojuice extends AbstractServlet {

    private static Logger logger = LoggerFactory.getLogger(ServletDiscojuice.class);
    
    /**
     * Parse the http language accept headers
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
     * 
     * @param acceptLanguageRequestHeader The language accept header
     * @return 
     */
    private List<Language> getAcceptLanguages(String acceptLanguageRequestHeader) {
        List<Language> languages = new ArrayList<Language>();
        logger.debug("AcceptLanguage = {}", acceptLanguageRequestHeader);
        
        if(acceptLanguageRequestHeader == null) {
            acceptLanguageRequestHeader = "en"; //default is general english
        }
        
        Pattern pLanguageRange = Pattern.compile("(.*);q=(.*)");
        Matcher mLanguageRange = null;
        String[] languageRanges = acceptLanguageRequestHeader.split(",");
        for(String languageRange : languageRanges) {
            mLanguageRange = pLanguageRange.matcher(languageRange);
            String language = "en";
            float q = 1; //default        
            if(mLanguageRange.matches()) {
                language = mLanguageRange.group(1);
                q = Float.parseFloat(mLanguageRange.group(2));
            } else { //no quality available
                language = languageRange;
            }
            
            languages.add(new Language(language, q));
        }
        
        //Sort the language by priority
        Collections.sort(languages);
        
        for(Language lang : languages) {
            logger.debug("Lang = {}, q = {}", lang.getId(), lang.getQ());
        }
        
        return languages;
    }
    
    /**
     * Parse init-parameter with language map configuration.
     * 
     * Format:
     * (key1,value1);(key2,value2);...(keyn,valuen)
     * 
     * @param languageMapInput The input string
     * @return A Map with all the key,value pairs
     */
    public Map<String,String> parseLanguageMap(String languageMapInput) {        
        Map<String,String>  languageMap = new HashMap<String,String>();
       
        String[] languageMapItems = languageMapInput.split(";");
        Pattern p = Pattern.compile("\\((.*),(.*)\\)");
        for(String languageMapItem : languageMapItems) {
            Matcher m = p.matcher(languageMapItem);
            if(m.matches()) {
                String key = m.group(1);
                String value = m.group(2);
                logger.debug("added language map item: ({},{})", key, value);
                languageMap.put(key, value);
            } else {
                logger.warn("Skipped language map item: {}", languageMapItem);
            }
        }
        
        return languageMap;
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String defaultLanguage = context.getInitParameter("default-language");
        String languageMapInput = context.getInitParameter("language-mappings");
        
        //Construct the language map based on the configured input
        Map<String,String>  languageMap = parseLanguageMap(languageMapInput);
        //Get the language preferences from the Accept header
        List<Language> languages = getAcceptLanguages(request.getHeader("Accept-Language"));

        //Select a language by processing all languages in the prefered order.
        //If no language matches, default to "defaultLanguage".
        String selectedLanguageId = defaultLanguage;
        
        for(Language lang : languages) {
            if(languageMap.containsKey(lang.getId())) {
                selectedLanguageId = languageMap.get(lang.getId());
                break;
            }
        }
        
        //load the template
        String templatePath = context.getRealPath("/WEB-INF/classes/");
        StringTemplateGroup group = new StringTemplateGroup("wayfgroup", templatePath, DefaultTemplateLexer.class);
        StringTemplate page = group.getInstanceOf("wayf");

        //inject the proper values into the template
        AbstractDiscoJuiceInjector inject = DiscoJuiceInjectorFactory.getDiscoJuice20Injector(context);
        page.setAttribute("title", inject.getTitle());
        
        String jsLang = "<script type=\"text/javascript\" src=\"./js/discojuice/lang/discojuice.dict."+selectedLanguageId+".js\"></script>";
        
        page.setAttribute("discojuice_language_inject", jsLang);
        page.setAttribute("discojuice_inject", inject.getDiscoJuiceInject());
        page.setAttribute("noscript", inject.getNoScriptFallback());
        //send response
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(page.toString());
    }     

    /**
     * Object encapsulating a language together with a quality factor.
     * Implements to Comparable interface to allow sorting on the quality factor.
     */
    private class Language implements Comparable<Language> {
        private final String id;
        private final float q;
        
        public Language(String id, float q) {
            this.id = id;
            this.q = q;
        }

        @Override
        public int compareTo(Language t) {
            if(t.getQ() < q) {
                return -1;
            } else if(t.getQ() > q) {
                return 1;
            } else {
                return 0;
            }
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @return the q
         */
        public float getQ() {
            return q;
        }

    }
}
