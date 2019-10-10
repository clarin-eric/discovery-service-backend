package nl.mpi.geoip;

import java.util.Map;
import javax.servlet.ServletContext;
import nl.mpi.shibboleth.metadata.QuartzListener;
import nl.mpi.shibboleth.metadata.rest.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Reference:
 *  http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
 * 
 * @author wilelb
 */
public class DatabaseDownloadJob implements Job {
    
    private final static Logger logger = LoggerFactory.getLogger(DatabaseDownloadJob.class);
    
    @Override
    public void execute(final JobExecutionContext ctx) throws JobExecutionException {
        logger.trace("Executing Job: "+getClass().getName());
        ServletContext sctxt = (ServletContext)ctx.getMergedJobDataMap().get(QuartzListener.QUARTZ_SC_KEY_NAME);
        Map<String, String> config = Configuration.loadConfiguration(sctxt);
        
        String tmp_dir = System.getProperty("java.io.tmpdir");
        if(!tmp_dir.endsWith("/")) {
            tmp_dir += "/";
        }
        String geo_ip_database = tmp_dir+DatabaseDownloader.DEFAULT_GEOLITE2_CITY_FILENAME;
        if(config != null && config.get(Configuration.GEO_IP_DATABASE) != null) {
            geo_ip_database = config.get(Configuration.GEO_IP_DATABASE);
        }
        
        logger.trace("Loaded database path: "+geo_ip_database);
        
        DatabaseDownloader downloader = new DatabaseDownloader(geo_ip_database);
        downloader.checkForUpdate();
        logger.trace("Job executed"); 
    }
}
