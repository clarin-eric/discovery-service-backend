package nl.mpi.shibboleth.metadata.rest;

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

    /**
     * Return a list of unique country codes used in the shibboleth metadata 
     * specified by the input xml file.
     * 
     * @param source
     * @return 
     */
    @POST
    @Path("/languages")
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON})
    public MetadataLanguageProcessor.Languages getLanguages(MetadataSource source) {
        if (source == null) {
            throw new IllegalStateException("No source argument supplied.");
        } else if (source.getMetadataSources() == null || source.getMetadataSources().size() <= 0) {
            throw new IllegalStateException("No source urls defined in the source argument.");
        }
        
        MetadataLanguageProcessor processor = new MetadataLanguageProcessor();
        processIdpDescriptors(source, processor);
        return processor.getLanguages(); 
    }

    /**
     * Given the shibboleth metadata files specified in the input xml file, generate
     * a list of idps in disojuice json format
     * 
     * @param source
     * @return
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
       
        GeoIpLookup lookup = Configuration.loadLookup(ctxt);
        MetadataDiscojuiceProcessor processor = new MetadataDiscojuiceProcessor(lookup);
        processIdpDescriptors(source, processor);
        
        List<DiscoJuiceJsonObject> objects = processor.getDiscoJuiceJson().getObjects();        
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
     */
    protected void processIdpDescriptors(MetadataSource source, MetadataProcessor processor) {
        for (String sourceUrl : source.getMetadataSources()) {
            try {
                logger.info("Processing: " + sourceUrl);

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
                t1 = System.nanoTime();
                for (EntityDescriptor descriptor : descriptors.getEntityDescriptor()) {
                    processor.process(descriptor, source);                    
                }
                t2 = System.nanoTime();

                logger.info(
                        "Processing finished for {} IDPs in {}ms",
                        processor.getIdpCount(),
                        (t2 - t1) / 1000000);

            } catch (MalformedURLException ex) {
                logger.error("", ex);
            }
        }
    }
}
