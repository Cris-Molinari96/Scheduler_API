package com.project.scheduler.scheduler;

import com.project.scheduler.entity.Job;
import com.project.scheduler.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

// Questo gestore comunica con i controller
@Component
public class SchedulerService {

    private TaskScheduler taskScheduler;
    private JobRegistry jobRegistry;
    private JobService jobService;

    @Autowired
    public SchedulerService(JobService jobService, JobRegistry jobRegistry, TaskScheduler taskScheduler) {
        this.jobRegistry = jobRegistry;
        this.jobService = jobService;
        this.taskScheduler = taskScheduler;
    }

    public boolean getStatusJob(String jobName) {
        if (jobRegistry.isJobRunning(jobName)) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, String> getStatusAllJobs() {
        Map<String, String> jobStatus = new HashMap<>();
        for (Map.Entry<String, ScheduledFuture<?>> entry : jobRegistry.getScheduledJobs().entrySet()) {
            String jobName = entry.getKey();
            ScheduledFuture<?> future = entry.getValue();
            String status = future.isCancelled() ? "Fermato" : "In esecuzione";
            jobStatus.put(jobName, status);
        }
        return jobStatus;
    }

    // ESEGUE IL JOB
    public void scheduleJob(Job job) {
        if (job != null) {
            boolean alreadyExecute = getStatusJob(job.getJobName());
            if (alreadyExecute) {
            } else {
                String jobName = job.getJobName();
                ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                        () -> System.out.println("Richiesta " + job.getApiURL()),
                        new CronTrigger("*/100 * * * * *")
                );
                jobRegistry.registerJob(jobName, scheduledTask);
            }
        }
    }

    public Job startOnlyJob(Long jobId) {
        if (jobId != null) {
            Job job = jobService.getJob(jobId);
            if (getStatusJob(job.getJobName())) {
                System.out.println("Sono già in esecuzione");
                return null;
            } else {
                scheduleJob(job);
                return job;
            }
        } else {
            return null;
        }
    }

    /* private Job searchJob(Long jobId){
         return executeJob( jobService.getJob(jobId));
     }*/
    public boolean stopJob(String jobName) {
        ScheduledFuture<?> scheduledTask = jobRegistry.getScheduledJobs().get(jobName);
        if (scheduledTask != null) {
            boolean cancelled = scheduledTask.cancel(true);
            if (cancelled) {
                jobRegistry.unregisterJob(jobName);
            }
            return cancelled;
        }

        return false;
    }


}
