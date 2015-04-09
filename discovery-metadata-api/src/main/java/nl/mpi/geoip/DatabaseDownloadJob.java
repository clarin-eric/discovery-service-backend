package nl.mpi.geoip;

import java.io.IOException;
import static java.util.Locale.lookup;
import java.util.Map;
import javax.servlet.ServletContext;
import nl.mpi.shibboleth.metadata.rest.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
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
        try {
            logger.trace("Executing Job: "+getClass().getName());
            ServletContext sctxt = (ServletContext)ctx.getScheduler().getContext().get("servletContext");
            Map<String, String> config = Configuration.loadConfiguration(sctxt);
            logger.trace("Loaded database path: "+config.get(Configuration.GEO_IP_DATABASE));
            DatabaseDownloader downloader = new DatabaseDownloader(config.get(Configuration.GEO_IP_DATABASE));
            downloader.checkForUpdate();
            logger.trace("Job executed");
        } catch(SchedulerException ex) {
            throw new JobExecutionException("Failed to retrieve servlet context from quartz scheduler", ex);
        } 
    }
}
