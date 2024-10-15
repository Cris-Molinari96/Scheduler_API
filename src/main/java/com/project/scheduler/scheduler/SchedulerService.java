package com.project.scheduler.scheduler;

import com.project.scheduler.entity.Job;
import com.project.scheduler.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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

    public boolean startAllJobs() {
        List<Job> jobsList = jobService.getAllJobs();
        if (jobsList.size() != 0) {
            for (Job job : jobsList) {
                scheduleJob(job);
            }
            return true;
        } else {
            return false;
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
                System.out.println("Job '" + jobName + "' fermato con successo.");
            } else {
                System.out.println("Impossibile fermare il job '" + jobName + "'. Potrebbe essere già completato o in esecuzione.");
            }
            return cancelled;
        } else {
            System.out.println("Job '" + jobName + "' non trovato nel registro.");
            return false;
        }
    }

    public boolean stopAllJob() {
        Map<String, ScheduledFuture<?>> allJobs = jobRegistry.getScheduledJobs();
        if (!allJobs.isEmpty()) {
            for (Map.Entry<String, ScheduledFuture<?>> entry : allJobs.entrySet()) {
                stopJob(entry.getKey());
            }
            return true;
        } else {
            return false;
        }
    }
}
