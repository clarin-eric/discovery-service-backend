package nl.mpi.shibboleth.metadata;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Reference:
 *   https://stackoverflow.com/questions/5663640/how-to-use-quartz-with-quartzinitializerlistener
 * 
 * @author wilelb
 */
@WebListener
public class QuartzListener extends QuartzInitializerListener {

    public final static String QUARTZ_SC_KEY_NAME = "servletContext";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        
        ServletContext ctx = sce.getServletContext();        
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        if(factory == null) {
            factory = new StdSchedulerFactory();
            ctx.setAttribute(QUARTZ_FACTORY_KEY, factory);
        }
        
        //pass the servlet context to the job
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(QUARTZ_SC_KEY_NAME, sce.getServletContext());
            
        try {
            JobDetail jobDetail = 
                JobBuilder
                        .newJob(nl.mpi.geoip.DatabaseDownloadJob.class)
                        .usingJobData(jobDataMap)
                        .build();
            
            Trigger trigger = 
                TriggerBuilder.newTrigger()
                        .withIdentity("simple")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                        .startNow()
                        .build();
            
            Scheduler scheduler = factory.getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            ctx.log("There was an error scheduling the job.", e);
        }
    }

}
