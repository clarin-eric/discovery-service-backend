package nl.mpi.geoip;

import javax.servlet.ServletContext;
import nl.mpi.shibboleth.metadata.QuartzListener;
import nl.mpi.shibboleth.metadata.Configuration;
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
        //Load geo ip database path
        ServletContext sctxt = (ServletContext)ctx.getMergedJobDataMap().get(QuartzListener.QUARTZ_SC_KEY_NAME);
        Configuration.loadConfiguration(sctxt);
        String geo_ip_database = Configuration.getGeoIpDatabaseFile();
        logger.trace("Loaded database path: "+geo_ip_database);
        //Run database download
        DatabaseDownloader downloader = new DatabaseDownloader(geo_ip_database);
        downloader.checkForUpdate();
        logger.trace("Job executed"); 
    }
}
