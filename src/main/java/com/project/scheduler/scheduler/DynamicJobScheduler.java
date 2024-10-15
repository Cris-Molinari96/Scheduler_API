package com.project.scheduler.scheduler;

import com.project.scheduler.entity.Job;
import com.project.scheduler.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

//! Utilizziamo il componente solo per i scheduler che devono essere avviati direttamente all'istanza dell'applicazione
@Component
public class DynamicJobScheduler {

    private JobService jobService;
    private TaskScheduler taskScheduler;
    private JobRegistry jobRegistry;

    @Autowired
    public DynamicJobScheduler(JobService jobService, TaskScheduler taskScheduler, JobRegistry jobRegistry) {
        this.jobService = jobService;
        this.taskScheduler = taskScheduler;
        this.jobRegistry = jobRegistry;
    }

    // Recupero di tutti i JOB nel database e li avvia
   /* @PostConstruct
    public void scheduleJobs() {
    }*/

    // Esecuzione dei singoli job, questo metodo viene utilizzato per eseguire i JOB anche negli altri controller
    // no duplicazione di codice
//    public String executeJob(Job job) {
//        if (job != null) {
//            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
//                    () -> executeJob(job),
//                    new CronTrigger("*/100 * * * * *")
//            );
//            jobRegistry.registerJob(job.getJobName(), scheduledTask);
//            return "Richiesta " + job.getApiURL();
//        }else{
//            return null;
//        }
//    }
}
