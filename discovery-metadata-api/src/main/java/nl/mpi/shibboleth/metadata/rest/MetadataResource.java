package nl.mpi.shibboleth.metadata.rest;

import eu.clarin.discovery.federation.Authorities;
import eu.clarin.discovery.federation.AuthoritiesFileParser;
import eu.clarin.discovery.federation.FileBackedAuthoritiesFileParser;
import eu.clarin.discovery.federation.AuthoritiesMapper;
import eu.clarin.report.ReporterFactory;
import java.io.File;
import nl.mpi.shibboleth.metadata.Configuration;
import java.io.FileOutputStream;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import nl.mpi.geoip.GeoIpLookup;
import nl.mpi.shibboleth.metadata.MetadataParser;
import nl.mpi.shibboleth.metadata.discojuice.DiscoJuiceJsonObject;
import nl.mpi.shibboleth.metadata.shibboleth.EntitiesDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.source.MetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CURL example: 
 *  curl -X POST \
 *      -d @source.xml \
 *      -H"Content-Type: application/xml" \
 *      -H"Accept: application/json"
 *      http://localhost:8080/sma/rest/metadata/discojuice > metadata_mpi.json
 * 
 * @author wilelb
 */
@Path("/metadata")
public class MetadataResource {

    private static final Logger logger = LoggerFactory.getLogger(MetadataResource.class);

    private final MetadataParser mdParser = new MetadataParser();

    @javax.ws.rs.core.Context
    private ServletContext ctxt;

    private AuthoritiesMapper getAuthoritiesMapper(URL federationMapSourceUrl, String fileLocation, String reportingPropertiesFile) {
        AuthoritiesFileParser parser = new FileBackedAuthoritiesFileParser(fileLocation);
        Authorities map = parser.parse(federationMapSourceUrl);
        AuthoritiesMapper mapper = new AuthoritiesMapper(map);
        ReporterFactory.loadFromProperties(reportingPropertiesFile, mapper);
        return mapper;
    }

    /**
     * Given the shibboleth metadata files specified in the input xml file, generate
     * a list of idps in disojuice json format
     * 
     * @param source
     * @return
     * @throws java.io.IOException
     */
    @POST
    @Path("/discojuice")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON  ) 
    public List<DiscoJuiceJsonObject> getDiscojuiceJson(MetadataSource source) throws IOException {
        if (source == null) {
            throw new IllegalStateException("No source argument supplied.");
        } else if (source.getMetadataSources() == null || source.getMetadataSources().size() <= 0) {
            throw new IllegalStateException("No source urls defined in the source argument.");
        }
        
        String conversionId = UUID.randomUUID().toString();
       
        long t1 = System.nanoTime();
        logger.info("[{}] Convert metadata request", conversionId);
         
        String reportingPropertiesFile = null;
        try {          
            reportingPropertiesFile = Configuration.getReportingPropertiesFile(ctxt);
        } catch(NullPointerException ex) {
            logger.warn("Failed to load reporting properties file. Continueing without reporting functionality.");
        }
        String dataDir = Configuration.getDataDir(ctxt);
        
        AuthoritiesMapper mapper = 
                getAuthoritiesMapper(
                    new URL(source.getFederationMapSource()), 
                    dataDir+"/authorities.xml", 
                    reportingPropertiesFile);
        
        GeoIpLookup lookup = null;
        try {
            lookup = Configuration.loadLookup(ctxt);
        } catch(NullPointerException ex) {
            logger.warn("Failed to load geo ip lookup database. Continueing without geo ip lookup functionality.");
        }
        
        MetadataDiscojuiceProcessor processor = new MetadataDiscojuiceProcessor(lookup, mapper);
        processIdpDescriptors(source, processor);
        
        //Report any mapping issues
        mapper.report();
        
        List<DiscoJuiceJsonObject> objects = processor.getDiscoJuiceJson().getObjects();        
        
        long tDelta = System.nanoTime()-t1;
        logger.info("[{}] Convert metadata request finished in {}ms, processed {} entities." , conversionId, String.format("%.3f", tDelta/1000000.0), objects.size());
        return objects;
    }
    
    static void writeOutput(String outputFile, String str) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            try (Writer out = new OutputStreamWriter(fos, Charset.forName("UTF8"))) {
                out.write(str);
            }
        } 
        catch (IOException ex) {
            logger.error("Failed to write:", ex);
        }
    }
    
    /**
     * Parse all shibboleth metadata files specified in the source and use the
     * given processor the process the parsed metadata.
     * 
     * @param source
     * @param processor 
     * @throws java.net.MalformedURLException 
     */
    protected void processIdpDescriptors(MetadataSource source, MetadataProcessor processor) throws MalformedURLException {               
        for (String sourceUrl : source.getMetadataSources()) {
            try {
                logger.info("Processing input: " + sourceUrl);

                //Load the metadata xml
                long t1 = System.nanoTime();
                URL url = new URL(sourceUrl);
                EntitiesDescriptor descriptors = mdParser.parse(url);
                long t2 = System.nanoTime();

                logger.info(
                        "Parsed SAML metadata; found {} entity descriptors in {}ms",
                        descriptors.getEntityDescriptor().size(),
                        (t2 - t1) / 1000000);

                //Get all idp descriptors and use them to create DiscoJuiceJsonObject
                //objects and add these to the list.               
                for (EntityDescriptor descriptor : descriptors.getEntityDescriptor()) {
                    processor.process(descriptor, source);                    
                }
             
            } catch (MalformedURLException ex) {
                logger.error("", ex);
            }
        }
    }
}
